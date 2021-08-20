import BuildConstants.author
import BuildConstants.fabricApiVersion
import BuildConstants.fabricLanguageKotlinVersion
import BuildConstants.fabricLoaderVersion
import BuildConstants.fabrikVersion
import BuildConstants.majorMinecraftVersion
import BuildConstants.minecraftVersion
import BuildConstants.yarnMappingsVersion

plugins {
    kotlin("jvm")

    id("fabric-loom")
}

group = "net.axay"
version = fabrikVersion

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
    languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    processResources {
        val properties = linkedMapOf(
            "id" to project.name,
            "version" to project.version,
            "description" to project.description,
            "author" to author,
            "minecraftversion" to "${majorMinecraftVersion}.x"
        )

        inputs.properties(properties)

        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }
}
