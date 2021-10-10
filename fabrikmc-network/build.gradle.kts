import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `dokka-script`
    kotlin("plugin.serialization")
}

val modName by extra("$projectTitle Network")

dependencies {
    implementation(project(":${rootProject.name}-core"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.3.0")
}
