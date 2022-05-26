package net.axay.fabrik.compose.internal

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.axay.fabrik.compose.icons.McIcon
import net.axay.fabrik.compose.mojangapi.LauncherMeta
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.logging.logError
import net.axay.fabrik.core.logging.logInfo
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.minecraft.SharedConstants
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

object AssetsLoader {
    private val assetsStorage = (Fabrik.currentServer?.serverDirectory ?: File(".")).resolve("server-assets/")
    private val assetsPath = assetsStorage.toPath().resolve("assets")

    private val loadedAssets = CompletableDeferred<Boolean>()

    init {
        fabrikCoroutineScope.launch(Dispatchers.IO) {
            val serverVersion = SharedConstants.getCurrentVersion().name
            val versionFile = assetsStorage.resolve("version.txt")

            if (
                (if (versionFile.exists()) versionFile.readText() != serverVersion else true) || !assetsStorage.resolve("assets").exists()
            ) {
                val clientFile = assetsStorage.resolve("client-${serverVersion}.jar")
                clientFile.parentFile.mkdirs()
                clientFile.createNewFile()
                LauncherMeta.downloadClientTo(clientFile, serverVersion)

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

    suspend fun loadImage(icon: McIcon): ImageBitmap? {
        loadedAssets.await()

        return withContext(Dispatchers.IO) {
            try {
                @Suppress("BlockingMethodInNonBlockingContext")
                assetsPath.resolve("minecraft/textures/${icon}").inputStream().buffered().use(::loadImageBitmap)
            } catch (ignored: NoSuchFileException) {
                logError("Cannot load or find image file for given icon: $icon")
                null
            }
        }
    }
}
