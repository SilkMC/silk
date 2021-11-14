package net.axay.fabrik.compose.internal

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.logging.logInfo
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.minecraft.SharedConstants
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import java.io.File
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

object AssetsLoader {
    private val assetsStorage = (Fabrik.currentServer?.runDirectory ?: File(".")).resolve("server-assets/")
    private val assetsPath = assetsStorage.toPath().resolve("assets")

    private val loadedAssets = CompletableDeferred<Boolean>()

    init {
        fabrikCoroutineScope.launch(Dispatchers.IO) {
            val serverVersion = SharedConstants.getGameVersion().name
            val versionFile = assetsStorage.resolve("version.txt")

            if (
                (if (versionFile.exists()) versionFile.readText() != serverVersion else true) || !assetsStorage.resolve("assets").exists()
            ) {
                val json = Json { ignoreUnknownKeys = true }

                // request version info
                logInfo("Requesting version info for version ${serverVersion}...")
                val versionInfoUrl = json.decodeFromString<VersionManifest>(URL("https://launchermeta.mojang.com/mc/game/version_manifest.json").readText())
                    .versions.find { it.id == serverVersion }?.url
                val clientDownloadUrl = json.decodeFromString<VersionInfo>(URL(versionInfoUrl).readText())
                    .downloads.client.url
                logInfo("Found version info for $serverVersion")

                // download client
                val clientFile = assetsStorage.resolve("client-${serverVersion}.jar")
                logInfo("Downloading ${clientFile.name}...")
                clientFile.parentFile.mkdirs()
                clientFile.createNewFile()
                clientFile.outputStream().channel.transferFrom(Channels.newChannel(URL(clientDownloadUrl).openStream()), 0, Long.MAX_VALUE)
                logInfo("Finished downloading the client")

                // extract assets
                logInfo("Extracting assets...")
                assetsStorage.resolve("assets").deleteRecursively()
                val assetsStoragePath = assetsStorage.toPath()
                FileSystems.newFileSystem(clientFile.toPath()).use { fileSystem ->
                    Files.walk(fileSystem.getPath("assets")).forEach { assetPath ->
                        if ((assetPath.isDirectory()) || assetPath.fileName.toString().endsWith("png"))
                            Files.copy(assetPath, assetsStoragePath.resolve(assetPath.pathString))
                    }
                }
                clientFile.delete()
                logInfo("Finished extracting assets")

                // save downloaded version
                versionFile.createNewFile()
                versionFile.writeText(serverVersion)
            }

            loadedAssets.complete(true)
        }
    }

    suspend fun loadImage(item: Item): ImageBitmap {
        loadedAssets.await()

        val id = Registry.ITEM.getId(item)
        if (id.namespace != "minecraft")
            error("Using textures of custom Minecraft mods (in this case ${id.namespace}) for compose is not supported")

        return withContext(Dispatchers.IO) {
            @Suppress("BlockingMethodInNonBlockingContext")
            assetsPath.resolve("minecraft/textures/item/${id.path}.png").inputStream().buffered().use(::loadImageBitmap)
        }
    }

    @Serializable
    private data class VersionManifest(val versions: List<Link>) {
        @Serializable
        data class Link(val id: String, val url: String)
    }

    @Serializable
    private data class VersionInfo(val downloads: Downloads) {
        @Serializable
        data class Downloads(val client: Client) {
            @Serializable
            data class Client(val url: String)
        }
    }
}
