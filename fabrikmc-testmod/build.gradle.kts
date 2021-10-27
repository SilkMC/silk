import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    kotlin("plugin.serialization")
    id("org.jetbrains.compose") version "1.0.0-beta1"
}

dependencies {
    implementation(project(":${rootProject.name}-commands"))
    implementation(project(":${rootProject.name}-compose"))
    implementation(project(":${rootProject.name}-core"))
    implementation(project(":${rootProject.name}-game"))
    implementation(project(":${rootProject.name}-igui"))
    implementation(project(":${rootProject.name}-network"))
    implementation(project(":${rootProject.name}-persistence"))

    implementation(compose.desktop.currentOs)
}

val modName by extra("$projectTitle Testmod")
val modEntrypoints by extra(linkedMapOf(
    "main" to listOf("net.axay.fabrik.test.Manager"),
    "client" to listOf("net.axay.fabrik.test.Manager"),
))
val modDepends by extra(
    linkedMapOf(
        "fabricloader" to ">=0.8.7"
    )
)

loom {
    runs {
        register("serverOffline") {
            server()
            property("online-mode", "false")
        }
    }
}
