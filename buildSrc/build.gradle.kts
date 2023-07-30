plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.quiltmc.org/repository/release")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    fun pluginDep(id: String, version: String) = "${id}:${id}.gradle.plugin:${version}"

    val kotlinVersion = "1.9.0"

    compileOnly(kotlin("gradle-plugin", embeddedKotlinVersion))
    runtimeOnly(kotlin("gradle-plugin", kotlinVersion))
    compileOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", embeddedKotlinVersion))
    runtimeOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", kotlinVersion))

    implementation(pluginDep("fabric-loom", "1.3-SNAPSHOT"))
    implementation(pluginDep("io.github.juuxel.loom-vineflower", "1.11.0"))
    implementation(pluginDep("com.modrinth.minotaur", "2.8.2"))

    implementation(pluginDep("io.papermc.paperweight.userdev", "1.5.5"))
    implementation(pluginDep("xyz.jpenilla.run-paper", "2.1.0"))

    val compileDokkaVersion = "1.8.20"
    val dokkaVersion = "1.8.20"

    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:$compileDokkaVersion")
    runtimeOnly("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    compileOnly("org.jetbrains.dokka:dokka-base:$compileDokkaVersion")
    runtimeOnly("org.jetbrains.dokka:dokka-base:$dokkaVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}
