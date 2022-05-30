plugins {
    idea
    `dokka-script-root`
}

repositories {
    mavenCentral()
}

allprojects {
    group = "net.axay"
    version = "1.8.0"
    if (this.name.startsWith("fabrikmc")) {
        description = "FabrikMC is an API for using FabricMC with Kotlin"
    }
}

idea {
    module {
        excludeDirs.add(file("docs"))
    }
}
