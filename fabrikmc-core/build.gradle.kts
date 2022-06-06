import BuildConstants.projectTitle

description = "FabrikMC Core provides a simple and stable Kotlin API for working with Minecraft"

plugins {
    `java-version-script`
    `mod-build-script`
    `project-publish-script`
    `kotest-script`
    `dokka-script`
    kotlin("plugin.serialization")
}

dependencies{
    api(modProject(":${rootProject.name}-nbt"))
}

val modName by extra("$projectTitle Core")
val modEntrypoints by extra(linkedMapOf(
    "main" to listOf("net.axay.fabrik.core.internal.FabrikKt::init"),
    "client" to listOf("net.axay.fabrik.core.internal.FabrikKt::initClient")
))
val modMixinFiles by extra(listOf("${rootProject.name}-core.mixins.json"))
