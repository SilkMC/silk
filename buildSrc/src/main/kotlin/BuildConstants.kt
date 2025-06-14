@file:Suppress("RemoveSingleExpressionStringTemplate", "ConstPropertyName")

import org.gradle.api.Project

object BuildConstants {
    val Project.isSnapshot get() = version.toString().endsWith("-SNAPSHOT")
    val Project.projectState get() = if (isSnapshot) "beta" else "release"

    const val projectTitle = "Silk"

    const val curseforgeId = "447425"
    const val modrinthId = "aTaCgKLW"
    const val githubRepo = "SilkMC/silk"

    val authors = listOf("jakobkmar", "_F0X")

    // check these values here: https://jakobk.net/mcdev
    const val majorMinecraftVersion = "1.21"
    const val minecraftVersion = "$majorMinecraftVersion.6-rc1"
    const val paperMinecraftVersion = "1.21.1"
    const val fabricLoaderVersion = "0.16.14"
    const val fabricLanguageKotlinVersion = "1.13.3+kotlin.2.1.21"

    const val kotestVersion = "5.9.1"
    const val mockkVersion = "1.13.12"

    val uploadModules = listOf(
        "commands",
        "core",
        "game",
        "igui",
        "nbt",
        "network",
        "persistence",
    )
}
