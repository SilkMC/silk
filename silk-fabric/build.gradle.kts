import BuildConstants.fabricApiVersion
import BuildConstants.projectTitle

description = "Silk Core provides a simple and stable Kotlin API for working with Minecraft"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `project-publish-script`
    `kotest-script`
    `dokka-script`
}

dependencies {
    modApi("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
}

val modName by extra("$projectTitle Fabric")
