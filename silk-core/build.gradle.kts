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

val modName by extra("$projectTitle Core")
val modEntrypoints by extra(linkedMapOf(
    "main" to listOf("net.axay.silk.core.internal.SilkEntrypoint"),
    "client" to listOf("net.axay.silk.core.internal.SilkEntrypoint"),
))
val modMixinFiles by extra(listOf("${rootProject.name}-core.mixins.json"))
