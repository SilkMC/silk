import BuildConstants.majorMinecraftVersion
import BuildConstants.projectTitle

description = "Silk Testmod provides examples usages of Silk features"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    kotlin("plugin.serialization")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

val silkPaperPlugin = configurations.register("silkPaperPlugin") { isTransitive = false }

dependencies {
    silkPaperPlugin(project(":${rootProject.name}-paper"))
    compileOnly("io.papermc.paper:paper-api:$majorMinecraftVersion.build.+")

    implementation(modProject(":${rootProject.name}-commands"))
    implementation(modProject(":${rootProject.name}-core"))
    implementation(modProject(":${rootProject.name}-game"))
    implementation(modProject(":${rootProject.name}-igui"))
    implementation(modProject(":${rootProject.name}-network"))
    implementation(modProject(":${rootProject.name}-persistence"))
}

val modName by extra("$projectTitle Testmod")
val modEntrypoints by extra(linkedMapOf(
    "main" to listOf("net.silkmc.silk.test.FabricTestMod"),
    "client" to listOf("net.silkmc.silk.test.FabricTestMod"),
))
val modDepends by extra(
    linkedMapOf(
        "fabricloader" to ">=0.8.7"
    )
)

tasks.register<xyz.jpenilla.runpaper.task.RunServer>("runPaper") {
    description = "Changes the name of runServer to runPaper"
    group = "paper"
    minecraftVersion(majorMinecraftVersion)
    runDirectory(layout.projectDirectory.dir("run-paper").asFile)
    pluginJars(silkPaperPlugin)
    pluginJars(tasks.jar.flatMap { it.archiveFile })
}

tasks.processResources {
    val props = mapOf(
        "description" to project.description,
        "version" to project.version,
        "mcVersion" to majorMinecraftVersion,
    )
    inputs.properties(props)
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}

idea {
    module {
        excludeDirs.add(file("run"))
        excludeDirs.add(file("run-paper"))
    }
}
