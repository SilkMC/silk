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

    const val majorMinecraftVersion = "1.17"

    // check these values here: https://axay.net/fabric/gradlekts/latest
    const val minecraftVersion = "1.17.1"
    const val yarnMappingsVersion = "1.17.1+build.63:v2"
    const val fabricLoaderVersion = "0.11.7"
    const val fabricApiVersion = "0.42.1+1.17"
    const val fabricLanguageKotlinVersion = "1.6.5+kotlin.1.5.31"

    const val kotestVersion = "4.6.3"
}
