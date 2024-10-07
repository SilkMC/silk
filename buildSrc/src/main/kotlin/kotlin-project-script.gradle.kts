import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    idea
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
    javadoc {
        options {
            this as CoreJavadocOptions
            addStringOption("Xdoclint:none", "-quiet")
        }
    }
}

kotlin {
    jvmToolchain(21)

    sourceSets.all {
        languageSettings {
            val whitelistModules = BuildConstants.uploadModules
            if (project.name.removePrefix(rootProject.name + "-") in whitelistModules) {
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

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
