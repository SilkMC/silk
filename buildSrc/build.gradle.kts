plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
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

    val kotlinVersion = "1.7.0"

    compileOnly(kotlin("gradle-plugin", embeddedKotlinVersion))
    runtimeOnly(kotlin("gradle-plugin", kotlinVersion))
    compileOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", embeddedKotlinVersion))
    runtimeOnly(pluginDep("org.jetbrains.kotlin.plugin.serialization", kotlinVersion))

    implementation(pluginDep("fabric-loom", "0.12-SNAPSHOT"))
    implementation(pluginDep("io.github.juuxel.loom-quiltflower", "1.7.2"))
    implementation(pluginDep("org.quiltmc.quilt-mappings-on-loom", "4.2.0"))
    implementation(pluginDep("com.matthewprenger.cursegradle", "1.4.0"))
    implementation(pluginDep("com.modrinth.minotaur", "2.2.0"))

    implementation(pluginDep("io.papermc.paperweight.userdev", "1.3.6"))
    implementation(pluginDep("xyz.jpenilla.run-paper", "1.0.6"))

    val compileDokkaVersion = "1.6.21"
    val dokkaVersion = "1.7.0"

    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:$compileDokkaVersion")
    runtimeOnly("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    compileOnly("org.jetbrains.dokka:dokka-base:$compileDokkaVersion")
    runtimeOnly("org.jetbrains.dokka:dokka-base:$dokkaVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}
