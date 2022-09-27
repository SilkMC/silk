import BuildConstants.projectTitle

description = "Silk NBT adds NBT to kotlinx.serialization and provides extensions for working with NBT in Kotlin"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `project-publish-script`
    `kotest-script`
    `dokka-script`
    kotlin("plugin.serialization")
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle NBT")
