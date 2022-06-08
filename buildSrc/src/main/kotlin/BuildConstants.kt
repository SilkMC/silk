import com.modrinth.minotaur.request.VersionType
import org.gradle.api.Project

object BuildConstants {
    val Project.isSnapshot get() = version.toString().endsWith("-SNAPSHOT")
    val Project.projectState get() = if (isSnapshot) "beta" else "release"
    val Project.projectStateType get() = if (isSnapshot) VersionType.BETA else VersionType.RELEASE

    const val projectTitle = "FabrikMC"

    const val curseforgeId = "447425"
    const val modrinthId = "aTaCgKLW"
    const val githubRepo = "jakobkmar/fabrikmc"

    val authors = listOf("jakobkmar", "_F0X")

    const val majorMinecraftVersion = "1.19"

    // check these values here: https://axay.net/mcdev
    const val minecraftVersion = "1.19"
    const val quiltMappingsVersion = "${minecraftVersion}+build.1:v2"
    const val fabricLoaderVersion = "0.14.7"
    const val fabricApiVersion = "0.55.2+1.19"
    const val fabricLanguageKotlinVersion = "1.7.4+kotlin.1.6.21"

    const val kotestVersion = "5.3.0"
    const val mockkVersion = "1.12.4"

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
