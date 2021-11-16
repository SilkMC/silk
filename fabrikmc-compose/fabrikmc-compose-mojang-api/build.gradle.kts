plugins {
    `java-version-script`
    `project-publish-script`
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}
