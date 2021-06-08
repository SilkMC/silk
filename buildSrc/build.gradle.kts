plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.5.10"))
    implementation("net.fabricmc", "fabric-loom", "0.8-SNAPSHOT")
    implementation("gradle.plugin.com.matthewprenger:CurseGradle:1.4.0")
    implementation("gradle.plugin.com.modrinth.minotaur:Minotaur:1.1.0")
}
