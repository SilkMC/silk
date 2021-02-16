val projectName = "fabrikmc"

rootProject.name = projectName

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
    }
}


include("$projectName-igui")
