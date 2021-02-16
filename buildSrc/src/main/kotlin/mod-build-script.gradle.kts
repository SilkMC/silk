import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.kotlin.dsl.*

/*
 * BUILD CONSTANTS
 */

// check these on https://modmuss50.me/fabric.html
val minecraftVersion = "1.16.5"
val fabricVersion = "0.30.0+1.16"
val yarnMappings = "1.16.5+build.4"
val loaderVersion = "0.11.1"

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")
}

/*
 * PROJECT
 */

group = "net.axay"
version = "0.0.1"

val status = "alpha"

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    maven("http://maven.fabricmc.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.4.30+build.2")
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

/**
 * PUBLISH
 */

tasks.withType<CurseUploadTask> {
    dependsOn(tasks.withType<RemapJarTask>())
}

curseforge {
    apiKey = property("curseforge.token") ?: ""
    project(closureOf<CurseProject> {
        id = "447425"

        releaseType = status
        addGameVersion(minecraftVersion)
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
