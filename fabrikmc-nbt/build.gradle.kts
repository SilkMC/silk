import BuildConstants.projectTitle

description = "FabrikMC NBT adds NBT to kotlinx.serialization and provides extensions for working with NBT in Kotlin"

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `kotest-script`
    `dokka-script`
    kotlin("plugin.serialization")
}

val modName by extra("$projectTitle NBT")
