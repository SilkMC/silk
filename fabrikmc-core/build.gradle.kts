import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `kotest-script`
    `dokka-script`
    kotlin("plugin.serialization")
}

val modName by extra("$projectTitle Core")
val modEntrypoints by extra(linkedMapOf("main" to listOf("net.axay.fabrik.core.FabrikKt::init")))
