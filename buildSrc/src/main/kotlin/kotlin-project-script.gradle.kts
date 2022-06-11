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
            release.set(17)
            encoding = "UTF-8"
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

kotlin.sourceSets.all {
    languageSettings {
        if (project.name.removePrefix(rootProject.name + "-") in BuildConstants.uploadModules) {
            listOf("InternalSilkApi", "DelicateSilkApi").forEach {
                optIn("net.axay.silk.core.annotations.${it}")
            }
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}
