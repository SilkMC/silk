package net.axay.fabrik.compose.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo
import net.axay.fabrik.compose.mojangapi.LauncherMeta
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.name

class MinecraftResourceGeneratorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        MinecraftResourceGenerator(
            environment.codeGenerator,
            environment.options["minecraft-version"] ?: error("Missing minecraft-version argument")
        )
}

class MinecraftResourceGenerator(
    private val codeGenerator: CodeGenerator,
    private val version: String,
) : SymbolProcessor {
    var invoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) return emptyList()
        invoked = true

        val packageName = "net.axay.fabrik.compose.icons"
        val fileName = "McIcons"

        val clientFile = Files.createTempFile("minecraft-client-$version", ".jar")
        LauncherMeta.downloadClientTo(clientFile.toFile(), version)

        val textureTypes = listOf("block", "item", "misc", "mob_effect", "painting", "particle")
        val textureTypeContents = mutableMapOf<String, MutableList<String>>()

        FileSystems.newFileSystem(clientFile).use { fileSystem ->
            textureTypes.forEach { textureType ->
                Files.walk(fileSystem.getPath("assets/minecraft/textures/${textureType}/")).forEach {
                    if (!it.isDirectory() && it.extension == "png") {
                        textureTypeContents.getOrPut(textureType) { ArrayList() } += it.name
                    }
                }
            }
        }

        val mcIconType = TypeVariableName("McIcon")

        @OptIn(KotlinPoetKspPreview::class)
        FileSpec.builder(packageName, fileName)
            .addFileComment("this file has been generated based on the client jar file of Minecraft $version")
            .addTypeAlias(TypeAliasSpec.builder("McIcon", String::class).build())
            .addType(TypeSpec.objectBuilder(fileName)
                .apply {
                    textureTypes.forEach { textureType ->
                        addType(TypeSpec.objectBuilder(textureType.fromMcToKt().replaceFirstChar(Char::titlecase))
                            .apply {
                                textureTypeContents[textureType]?.forEach {
                                    addProperty(PropertySpec.builder(it.fromMcToKt(), mcIconType, KModifier.CONST)
                                        .initializer("\"${textureType}/${it}\"")
                                        .build())
                                }
                            }
                            .build())
                    }
                }
                .build())
            .build()
            .writeTo(codeGenerator, Dependencies(false))

        return emptyList()
    }

    private fun String.fromMcToKt(): String {
        return trim()
            .removeSuffix(".png")
            .split("_")
            .joinToString("") { it.replaceFirstChar(Char::titlecase) }
            .replaceFirstChar(Char::lowercase)
    }
}
