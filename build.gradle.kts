plugins {
    idea
    `dokka-script-root`
}

repositories {
    mavenCentral()
}

allprojects {
    group = "net.silkmc"
    version = "1.11.1"
    if (this.name.startsWith("silk")) {
        description = "Silk is a Minecraft API for Kotlin"
    }
}

idea {
    module {
        excludeDirs.add(file("docs"))
    }
}
