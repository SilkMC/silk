import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
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
            freeCompilerArgs += "-Xcontext-receivers"
        }
    }
}

kotlin.sourceSets.all {
    languageSettings {
        if (project.name.removePrefix(rootProject.name + "-") in BuildConstants.uploadModules) {
            listOf("InternalSilkApi", "DelicateSilkApi", "ExperimentalSilkApi").forEach {
                optIn("net.silkmc.silk.core.annotations.${it}")
            }
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}
