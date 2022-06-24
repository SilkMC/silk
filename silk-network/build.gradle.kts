import BuildConstants.projectTitle

description = "Silk Network adds easy-to-use custom packet support with kotlinx.serialization"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `project-publish-script`
    `dokka-script`
    kotlin("plugin.serialization")
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Network")
val modEntrypoints by extra(linkedMapOf(
    "main" to listOf("net.silkmc.silk.network.internal.SilkNetwork"),
    "client" to listOf("net.silkmc.silk.network.internal.SilkNetwork"),
))
