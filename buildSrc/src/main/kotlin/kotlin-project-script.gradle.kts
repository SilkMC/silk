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
