import BuildConstants.minecraftVersion
import BuildConstants.projectTitle

description = "FabrikMC Compose brings Kotlin compose-jb to Minecraft"

plugins {
    `java-version-script`
    `mod-build-script`
    `project-publish-script`
    `dokka-script`
    kotlin("plugin.serialization")
    id("org.jetbrains.compose") version "1.0.1-rc2"
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
    include(api(project(":${rootProject.name}-compose:${rootProject.name}-compose-mojang-api")) {
        exclude("org.apache.logging.log4j", "log4j-api")
    })
    ksp(project(":${rootProject.name}-compose:${rootProject.name}-compose-ksp"))

    compileOnly(compose.desktop.common)

    api("org.jetbrains.kotlinx:multik-api:0.1.1")
    api("org.jetbrains.kotlinx:multik-default:0.1.1")

    api("com.github.ajalt.colormath:colormath:3.2.0")
}

val modName by extra("$projectTitle Compose")
val modMixinFiles by extra(listOf("${rootProject.name}-compose.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*", "${rootProject.name}-commands" to "*"))

ksp {
    arg("minecraft-version", minecraftVersion)
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}
