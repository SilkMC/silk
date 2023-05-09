import BuildConstants.minecraftVersion

plugins {
    kotlin("jvm")
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    paperweight.paperDevBundle("${minecraftVersion}-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
