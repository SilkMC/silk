val projectName = "fabrikmc"
rootProject.name = projectName

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    apply(from = "shared.properties.gradle.kts")
    val kotlinVersion: String by settings

    plugins {
        kotlin("plugin.serialization") version kotlinVersion
    }
}

include("$projectName-all")

include("$projectName-commands")
include("$projectName-compose")
include("$projectName-core")
include("$projectName-game")
include("$projectName-igui")
include("$projectName-nbt")
include("$projectName-network")
include("$projectName-persistence")

include("$projectName-testmod")
