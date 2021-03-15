import BuildConstants.fabricApiVersion
import BuildConstants.fabricLanguageKotlinVersion
import BuildConstants.fabrikVersion
import BuildConstants.loaderVersion
import BuildConstants.minecraftVersion
import BuildConstants.yarnMappingsVersion
import org.gradle.kotlin.dsl.*

/*
 * BUILD CONSTANTS
 */

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")

    kotlin("plugin.serialization")
}

/*
 * PROJECT
 */

group = "net.axay"
version = fabrikVersion

description = "FabrikMC is an API for using FabricMC with Kotlin."

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    maven("http://maven.fabricmc.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappingsVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
}

/*
 * BUILD
 */

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
