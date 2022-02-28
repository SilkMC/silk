import BuildConstants.minecraftVersion
import BuildConstants.projectTitle

description = "FabrikMC Compose brings Kotlin compose-jb to Minecraft"

plugins {
    `java-version-script`
    `mod-build-script`
    `project-publish-script`
    `dokka-script`
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
}

val includeTransitive: Configuration by configurations.creating

dependencies {
    api(modProject(":${rootProject.name}-core"))
    include(api(project(":${rootProject.name}-compose:${rootProject.name}-compose-mojang-api"))!!)
    ksp(project(":${rootProject.name}-compose:${rootProject.name}-compose-ksp"))

    includeTransitive(api("org.jetbrains.kotlinx:multik-api:0.1.1")!!)
    includeTransitive(api("org.jetbrains.kotlinx:multik-jvm:0.1.1")!!)

    includeTransitive(api("com.github.ajalt.colormath:colormath:3.2.0")!!)

    includeTransitive(api(compose.desktop.common)!!)
    includeTransitive(compose.desktop.linux_x64)
    includeTransitive(compose.desktop.linux_arm64)
    includeTransitive(compose.desktop.windows_x64)
    includeTransitive(compose.desktop.macos_x64)
    includeTransitive(compose.desktop.macos_arm64)

    handleIncludes(project, includeTransitive)
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
