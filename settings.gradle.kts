val projectName = "fabrikmc"

rootProject.name = projectName

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
    }
}


include("$projectName-core")
include("$projectName-commands")
include("$projectName-igui")
include("$projectName-nbt")
include("$projectName-persistence")

include("$projectName-testmod")
