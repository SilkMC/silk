plugins {
    kotlin("jvm")
    idea
}

repositories {
    mavenCentral()
    google()
}

tasks {
    javadoc {
        options {
            this as CoreJavadocOptions
            addStringOption("Xdoclint:none", "-quiet")
        }
    }
}

kotlin {
    jvmToolchain(25)

    sourceSets.all {
        languageSettings {
            // nbt does not depend on core, so the opt-in markers would be unresolved there
            val whitelistModules = BuildConstants.uploadModules.plus("paper").minus("nbt")
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
