import BuildConstants.projectTitle

description = "FabrikMC Testmod provides examples usages of FabrikMC features"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(modProject(":${rootProject.name}-commands"))
    implementation(modProject(":${rootProject.name}-core"))
    implementation(modProject(":${rootProject.name}-game"))
    implementation(modProject(":${rootProject.name}-igui"))
    implementation(modProject(":${rootProject.name}-network"))
    implementation(modProject(":${rootProject.name}-persistence"))
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
