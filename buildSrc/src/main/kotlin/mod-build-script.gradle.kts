import BuildConstants.authors
import BuildConstants.fabricApiVersion
import BuildConstants.fabricLanguageKotlinVersion
import BuildConstants.fabricLoaderVersion
import BuildConstants.githubRepo
import BuildConstants.majorMinecraftVersion
import BuildConstants.minecraftVersion
import BuildConstants.parchmentMappingsVersion
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

plugins {
    kotlin("jvm")
    id("fabric-loom")
    id("io.github.juuxel.loom-quiltflower")
    id("org.quiltmc.quilt-mappings-on-loom")
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.parchmentmc.org")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-${parchmentMappingsVersion}@zip")
        officialMojangMappings()
    })

    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
}

@Serializable
data class FabricModConfiguration(
    val schemaVersion: Int,
    val id: String,
    val version: String,
    val name: String,
    val description: String,
    val authors: List<String>,
    val entrypoints: LinkedHashMap<String, List<Entrypoint>> = linkedMapOf(),
    val mixins: List<String> = emptyList(),
    val depends: LinkedHashMap<String, String>,
    val contact: Contact,
    val license: String,
    val icon: String? = null,
    val custom: Custom? = null,
) {
    @Serializable
    data class Contact(
        val homepage: String,
        val issues: String,
        val sources: String,
        val discord: String,
    )

    @Serializable
    data class Entrypoint(
        val adapter: String,
        val value: String,
    )

    @Serializable
    data class Custom(
        val modmenu: ModMenu? = null,
    ) {
        @Serializable
        data class ModMenu(
            val parent: String,
        )
    }
}

val modName: String by extra
val modEntrypoints: LinkedHashMap<String, List<String>>? by extra(null)
val modMixinFiles: List<String>? by extra(null)
val modDepends: LinkedHashMap<String, String>? by extra(null)
val isModParent by extra(false)

tasks {
    val modDotJsonTask = register("modDotJson") {
        val modConfig = FabricModConfiguration(
            1,
            project.name,
            project.version.toString(),
            modName,
            project.description.toString(),
            authors,
            modEntrypoints?.mapValuesTo(LinkedHashMap()) {
                it.value.map { target -> FabricModConfiguration.Entrypoint("kotlin", target) }
            } ?: linkedMapOf(),
            modMixinFiles ?: emptyList(),
            linkedMapOf(
                "fabric-api" to "*",
                "fabric-language-kotlin" to ">=1.8.0+kotlin.1.7.0",
                "minecraft" to "${majorMinecraftVersion}.x"
            ).apply { putAll(modDepends ?: emptyMap()) },
            FabricModConfiguration.Contact(
                "https://github.com/$githubRepo",
                "https://github.com/$githubRepo/issues",
                "https://github.com/$githubRepo",
                "https://discord.gg/CJDUVuJ"
            ),
            "GPL-3.0-or-later",
            if (project.name.endsWith("-all")) "assets/${project.name}/icon.png" else null,
            if (isModParent) null else FabricModConfiguration.Custom(FabricModConfiguration.Custom.ModMenu("silk-all")),
        )

        val modDotJson = buildDir.resolve("resources/main/fabric.mod.json")

        inputs.property("modConfig", modConfig.toString())
        outputs.file(modDotJson)

        doFirst {
            val prettyJson = Json { prettyPrint = true }

            if (!modDotJson.exists()) {
                modDotJson.parentFile.mkdirs()
                modDotJson.createNewFile()
            }

            modDotJson.writeText(prettyJson.encodeToString(modConfig))
        }
    }

    processResources {
        dependsOn(modDotJsonTask)
    }
}
