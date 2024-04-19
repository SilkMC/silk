import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    google()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xcontext-receivers"
        }
    }
}

kotlin {
    jvmToolchain(21)

    sourceSets.all {
        languageSettings {
            if (project.name.removePrefix(rootProject.name + "-") in BuildConstants.uploadModules) {
                listOf("InternalSilkApi", "DelicateSilkApi", "ExperimentalSilkApi").forEach {
                    optIn("net.silkmc.silk.core.annotations.${it}")
                }
            }
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}
