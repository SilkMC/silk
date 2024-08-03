import BuildConstants.fabricApiVersion
import BuildConstants.projectTitle

description = "Silk Core provides a simple and stable Kotlin API for working with Minecraft"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `project-publish-script`
    `kotest-script`
    `dokka-script`
    kotlin("plugin.serialization")
}

dependencies {
    modApi("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    modCompileOnly("me.lucko:fabric-permissions-api:0.3.1")
}

val modName by extra("$projectTitle Core")
val modEntrypoints by extra(linkedMapOf(
    "main" to listOf("net.silkmc.silk.core.internal.SilkFabricEntrypoint"),
    "client" to listOf("net.silkmc.silk.core.internal.SilkFabricEntrypoint"),
))
val modMixinFiles by extra(listOf("${rootProject.name}-core.mixins.json"))
