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
    gradlePluginPortal()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.quiltmc.org/repository/release")
    maven("https://server.bbkr.space/artifactory/libs-release/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    fun pluginDep(id: String, version: String) = "${id}:${id}.gradle.plugin:${version}"

    implementation(kotlin("gradle-plugin", kotlinVersion))

    implementation(pluginDep("org.jetbrains.compose", "1.0.1"))
    implementation(pluginDep("com.google.devtools.ksp", kspVersion))

    implementation(pluginDep("fabric-loom", "0.12-SNAPSHOT"))
    implementation(pluginDep("io.github.juuxel.loom-quiltflower", "1.7.1"))
    implementation(pluginDep("org.quiltmc.quilt-mappings-on-loom", "4.0.0"))
    implementation(pluginDep("com.matthewprenger.cursegradle", "1.4.0"))
    implementation(pluginDep("com.modrinth.minotaur", "2.1.1"))

    implementation(pluginDep("io.papermc.paperweight.userdev", "1.3.5"))
    implementation(pluginDep("xyz.jpenilla.run-paper", "1.0.6"))

    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-base:$dokkaVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}
