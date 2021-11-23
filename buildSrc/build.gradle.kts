apply(from = "${rootDir.parentFile}/shared.properties.gradle.kts")
val kotlinVersion: String by extra
val dokkaVersion: String by extra

plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", kotlinVersion))

    implementation("net.fabricmc:fabric-loom:0.10-SNAPSHOT")
    implementation("gradle.plugin.com.matthewprenger:CurseGradle:1.4.0")
    implementation("gradle.plugin.com.modrinth.minotaur:Minotaur:1.2.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-base:$dokkaVersion")
    implementation("org.jetbrains.dokka:versioning-plugin:$dokkaVersion")
}
