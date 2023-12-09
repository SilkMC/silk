import org.gradle.api.Project

object BuildConstants {
    val Project.isSnapshot get() = version.toString().endsWith("-SNAPSHOT")
    val Project.projectState get() = if (isSnapshot) "beta" else "release"

    const val projectTitle = "Silk"

    const val curseforgeId = "447425"
    const val modrinthId = "aTaCgKLW"
    const val githubRepo = "SilkMC/silk"

    val authors = listOf("jakobkmar", "_F0X")

    const val majorMinecraftVersion = "1.20"

    // check these values here: https://jakobk.net/mcdev
    const val minecraftVersion = "1.20.4"
    const val fabricLoaderVersion = "0.15.1"
    const val fabricLanguageKotlinVersion = "1.10.10+kotlin.1.9.10"

    const val kotestVersion = "5.7.1"
    const val mockkVersion = "1.13.7"

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
