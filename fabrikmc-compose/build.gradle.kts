import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    id("org.jetbrains.compose") version "1.0.0-beta5"
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(project(":${rootProject.name}-core"))
    implementation(project(":${rootProject.name}-commands"))

    api("org.jetbrains.kotlinx:multik-api:0.1.0")
    api("org.jetbrains.kotlinx:multik-default:0.1.0")

    api("com.github.ajalt.colormath:colormath:3.1.1")
}

val modName by extra("$projectTitle Compose")
val modMixinFiles by extra(listOf("${rootProject.name}-compose.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*", "${rootProject.name}-commands" to "*"))
