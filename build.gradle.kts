tasks {
    register("uploadMod") {
        group = "upload"

        val fabrikmcAllProject = project(":${rootProject.name}-all")

        dependsOn(fabrikmcAllProject.tasks.named("uploadModrinth"))
        dependsOn(fabrikmcAllProject.tasks.named("curseforge"))
    }
}
