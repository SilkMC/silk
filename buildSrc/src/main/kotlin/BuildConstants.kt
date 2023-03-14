import org.gradle.api.Project

object BuildConstants {
    val Project.isSnapshot get() = version.toString().endsWith("-SNAPSHOT")
    val Project.projectState get() = if (isSnapshot) "beta" else "release"

    const val projectTitle = "Silk"

    const val curseforgeId = "447425"
    const val modrinthId = "aTaCgKLW"
    const val githubRepo = "SilkMC/silk"

    val authors = listOf("jakobkmar", "_F0X")

    const val majorMinecraftVersion = "1.19"

    // check these values here: https://jakobk.net/mcdev
    const val minecraftVersion = "1.19.4"
    const val parchmentMappingsVersion = "1.19.3:2023.03.12"
    const val fabricLoaderVersion = "0.14.17"
    const val fabricApiVersion = "0.75.3+1.19.4"
    const val fabricLanguageKotlinVersion = "1.9.1+kotlin.1.8.10"

    const val kotestVersion = "5.5.5"
    const val mockkVersion = "1.13.4"

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
