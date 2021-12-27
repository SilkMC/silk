apply(from = "${rootDir.parentFile}/shared.properties.gradle.kts")
val kotlinVersion: String by extra
val dokkaVersion: String by extra
val kspVersion: String by extra

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
    fun pluginDep(id: String, version: String) = "${id}:${id}.gradle.plugin:${version}"

    implementation(kotlin("gradle-plugin", kotlinVersion))

    implementation(pluginDep("org.jetbrains.compose", "1.0.1"))
    implementation(pluginDep("com.google.devtools.ksp", kspVersion))

    implementation(pluginDep("fabric-loom", "0.10-SNAPSHOT"))
    implementation(pluginDep("com.matthewprenger.cursegradle", "1.4.0"))
    implementation(pluginDep("com.modrinth.minotaur", "1.2.1"))

    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-base:$dokkaVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}
