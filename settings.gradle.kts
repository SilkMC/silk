val projectName = "silk"
rootProject.name = projectName

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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

include("$projectName-paper")
include("$projectName-fabric")

include("$projectName-testmod")
