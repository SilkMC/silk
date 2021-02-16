plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.4.30"))
    implementation("net.fabricmc", "fabric-loom", "0.5-SNAPSHOT")
    implementation("gradle.plugin.com.matthewprenger:CurseGradle:1.4.0")
}
