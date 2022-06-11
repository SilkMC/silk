plugins {
    `java-version-script`
    `project-publish-script`
    `dokka-script-noop`
    kotlin("plugin.serialization")
}

description = "Silk Compose Mojang API extracts Minecraft assets from the client jar"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}
