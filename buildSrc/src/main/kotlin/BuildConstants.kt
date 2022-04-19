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

    const val majorMinecraftVersion = "1.18"

    // check these values here: https://axay.net/mcdev
    const val minecraftVersion = "1.18.2"
    const val quiltMappingsVersion = "${minecraftVersion}+build.22:v2"
    const val fabricLoaderVersion = "0.13.3"
    const val fabricApiVersion = "0.51.0+1.18.2"
    const val fabricLanguageKotlinVersion = "1.7.2+kotlin.1.6.20"

    const val kotestVersion = "5.2.3"
}
