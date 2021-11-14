import BuildConstants.projectTitle

description = "FabrikMC Compose brings Kotlin compose-jb to Minecraft"

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `dokka-script`
    kotlin("plugin.serialization")
    id("org.jetbrains.compose") version "1.0.0-beta5"
}

dependencies {
    compileOnly(compose.desktop.common)
    api(modProject(":${rootProject.name}-core"))

    api("org.jetbrains.kotlinx:multik-api:0.1.1")
    api("org.jetbrains.kotlinx:multik-default:0.1.1")

    api("com.github.ajalt.colormath:colormath:3.1.1")
}

val modName by extra("$projectTitle Compose")
val modMixinFiles by extra(listOf("${rootProject.name}-compose.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*", "${rootProject.name}-commands" to "*"))
