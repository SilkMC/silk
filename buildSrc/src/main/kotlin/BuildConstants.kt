import com.modrinth.minotaur.request.VersionType

object BuildConstants {
    const val fabrikVersion = "1.2.0"
    val isSnapshot = fabrikVersion.endsWith("-SNAPSHOT")
    val projectState = if (isSnapshot) "beta" else "release"
    val projectStateType = if (isSnapshot) VersionType.BETA else VersionType.RELEASE

    const val curseforgeId = "447425"
    const val githubRepo = "bluefireoly/fabrikmc"

    const val author = "bluefireoly"

    const val majorMinecraftVersion = "1.17"

    // check these values here: https://axay.net/fabric/gradlekts/latest
    const val minecraftVersion = "1.17.1"
    const val yarnMappingsVersion = "1.17.1+build.39:v2"
    const val fabricLoaderVersion = "0.11.6"
    const val fabricApiVersion = "0.38.0+1.17"
    const val fabricLanguageKotlinVersion = "1.6.3+kotlin.1.5.21"

    const val kotestVersion = "4.6.1"
}
