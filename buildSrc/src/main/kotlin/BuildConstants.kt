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
    const val minecraftVersion = "1.20.1"
    const val parchmentMappingsVersion = "1.19.3:2023.03.12"
    const val fabricLoaderVersion = "0.14.21"
    const val fabricApiVersion = "0.83.1+1.20.1"
    const val fabricLanguageKotlinVersion = "1.9.4+kotlin.1.8.21"

    const val kotestVersion = "5.6.2"
    const val mockkVersion = "1.13.5"

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
