plugins {
    `java-version-script`
    `project-publish-script`
    `dokka-script-noop`
    kotlin("plugin.serialization")
}

group = "net.axay"
version = rootProject.version

description = "FabrikMC Compose Mojang API extracts Minecraft assets from the client jar"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}
