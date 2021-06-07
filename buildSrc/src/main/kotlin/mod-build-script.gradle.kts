import BuildConstants.author
import BuildConstants.fabricApiVersion
import BuildConstants.fabricLanguageKotlinVersion
import BuildConstants.fabricLoaderVersion
import BuildConstants.fabrikVersion
import BuildConstants.minecraftVersion
import BuildConstants.yarnMappingsVersion
import org.gradle.kotlin.dsl.*

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")
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
            "author" to author
        )

        inputs.properties(properties)

        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
