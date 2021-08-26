plugins {
    id("org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}

tasks {
    register("uploadMod") {
        group = "upload"

        val fabrikmcAllProject = project(":${rootProject.name}-all")

        dependsOn(fabrikmcAllProject.tasks.named("uploadModrinth"))
        dependsOn(fabrikmcAllProject.tasks.named("curseforge"))
    }
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(rootDir.resolve("dokka/build"))

    includes.from("dokka/includes/main.md")
}
