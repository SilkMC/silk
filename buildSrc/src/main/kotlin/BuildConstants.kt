object BuildConstants {
    const val fabrikVersion = "0.4.2"
    val isSnapshot = fabrikVersion.endsWith("-SNAPSHOT")
    val projectState = if (isSnapshot) "beta" else "release"

    const val curseforgeId = "447425"
    const val githubRepo = "bluefireoly/fabrikmc"

    const val author = "bluefireoly"

    // check these values here: https://axay.net/fabric/gradlekts/latest
    const val minecraftVersion = "1.17-rc2"
    const val yarnMappingsVersion = "1.17-rc2+build.10:v2"
    const val fabricLoaderVersion = "0.11.3"
    const val fabricApiVersion = "0.34.9+1.17"
    const val fabricLanguageKotlinVersion = "1.6.1+kotlin.1.5.10"
}
