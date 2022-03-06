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

    // check these values here: https://axay.net/fabric
    const val minecraftVersion = "1.18.2"
    const val quiltMappingsVersion = "${minecraftVersion}+build.3:v2"
    const val fabricLoaderVersion = "0.13.3"
    const val fabricApiVersion = "0.47.9+1.18.2"
    const val fabricLanguageKotlinVersion = "1.7.1+kotlin.1.6.10"

    const val kotestVersion = "5.0.2"
}
