import BuildConstants.authors
import BuildConstants.fabricApiVersion
import BuildConstants.fabricLanguageKotlinVersion
import BuildConstants.fabricLoaderVersion
import BuildConstants.majorMinecraftVersion
import BuildConstants.minecraftVersion
import BuildConstants.yarnMappingsVersion
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

plugins {
    kotlin("jvm")

    id("fabric-loom")
}

group = "net.axay"
version = rootProject.version

description = "FabrikMC is an API for using FabricMC with Kotlin."

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappingsVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

java {
    withSourcesJar()
    withJavadocJar()
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
) {
    @Serializable
    data class Entrypoint(
        val adapter: String,
        val value: String,
    )
}

val modName: String by extra
val modEntrypoints: LinkedHashMap<String, List<String>>? by extra(null)
val modMixinFiles: List<String>? by extra(null)
val modDepends: LinkedHashMap<String, String>? by extra(null)

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
                "fabric" to "*",
                "fabric-language-kotlin" to ">=1.6.0+kotlin.1.5.0",
                "minecraft" to "${majorMinecraftVersion}.x"
            ).apply { putAll(modDepends ?: emptyMap()) }
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
