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

    val kotlinVersion = "2.0.21"

    compileOnly(kotlin("gradle-plugin", embeddedKotlinVersion))
    runtimeOnly(kotlin("gradle-plugin", kotlinVersion))
    compileOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", embeddedKotlinVersion))
    runtimeOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", kotlinVersion))

    implementation(pluginDep("fabric-loom", "1.8-SNAPSHOT"))
    implementation(pluginDep("com.modrinth.minotaur", "2.8.7"))

    implementation(pluginDep("io.papermc.paperweight.userdev", "2.0.0-SNAPSHOT"))
    implementation(pluginDep("xyz.jpenilla.run-paper", "2.3.0"))

    val compileDokkaVersion = "1.9.20"
    val dokkaVersion = "1.9.20"

    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:$compileDokkaVersion")
    runtimeOnly("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    compileOnly("org.jetbrains.dokka:dokka-base:$compileDokkaVersion")
    runtimeOnly("org.jetbrains.dokka:dokka-base:$dokkaVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}
