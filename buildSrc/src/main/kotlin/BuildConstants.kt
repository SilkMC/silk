import org.gradle.api.Project

object BuildConstants {
    val Project.isSnapshot get() = version.toString().endsWith("-SNAPSHOT")
    val Project.projectState get() = if (isSnapshot) "beta" else "release"

    const val projectTitle = "Silk"

    const val curseforgeId = "447425"
    const val modrinthId = "aTaCgKLW"
    const val githubRepo = "SilkMC/silk"

    val authors = listOf("jakobkmar", "_F0X")

    const val majorMinecraftVersion = "1.21"

    // check these values here: https://jakobk.net/mcdev
    const val minecraftVersion = "1.21-rc1"
    const val fabricLoaderVersion = "0.15.11"
    const val fabricLanguageKotlinVersion = "1.11.0+kotlin.2.0.0"

    const val kotestVersion = "5.9.1"
    const val mockkVersion = "1.13.11"

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
