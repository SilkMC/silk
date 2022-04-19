import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration

plugins {
    id("org.jetbrains.dokka")
    idea
}

repositories {
    mavenCentral()
}

allprojects {
    group = "net.axay"
    version = "1.7.3"
    if (this.name.startsWith("fabrikmc")) {
        description = "FabrikMC is an API for using FabricMC with Kotlin"
    }
}

tasks {
    dokkaHtmlMultiModule {
        outputDirectory.set(projectDir.resolve("docs"))

        includes.from("dokka/includes/main.md")

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customStyleSheets = listOf(*(rootDir.resolve("dokka/stylesheets").listFiles() ?: emptyArray()))
        }
    }
}

idea {
    module {
        excludeDirs.add(file("docs"))
    }
}
