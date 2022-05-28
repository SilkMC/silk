val projectName = "fabrikmc"
rootProject.name = projectName

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    val kotlinVersion = "1.6.21"
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jetbrains.dokka") version kotlinVersion
    }
}

include("$projectName-all")

include("$projectName-commands")
include("$projectName-core")
include("$projectName-game")
include("$projectName-igui")
include("$projectName-nbt")
include("$projectName-network")
include("$projectName-persistence")

//include("$projectName-paper")

include("$projectName-testmod")
