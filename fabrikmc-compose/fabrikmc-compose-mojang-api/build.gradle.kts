plugins {
    `java-version-script`
    `project-publish-script`
    `dokka-script-noop`
    kotlin("plugin.serialization")
}

description = "FabrikMC Compose Mojang API extracts Minecraft assets from the client jar"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}
