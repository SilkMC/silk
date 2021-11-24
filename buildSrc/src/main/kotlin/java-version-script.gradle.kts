import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

tasks {
    withType<JavaCompile> {
        options.apply {
            release.set(16)
            encoding = "UTF-8"
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
        }
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

java {
    withSourcesJar()
    withJavadocJar()
}
