plugins {
    idea
    `dokka-script-root`
}

repositories {
    mavenCentral()
}

allprojects {
    group = "net.axay"
    version = "1.7.5"
    if (this.name.startsWith("fabrikmc")) {
        description = "FabrikMC is an API for using FabricMC with Kotlin"
    }
}

idea {
    module {
        excludeDirs.add(file("docs"))
    }
}
