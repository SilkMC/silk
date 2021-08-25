pluginManagement {
    apply(from = "${rootDir.parentFile}/shared.properties.gradle.kts")
    val kotlinVersion: String by extra

    plugins {
        kotlin("plugin.serialization") version kotlinVersion
    }
}
