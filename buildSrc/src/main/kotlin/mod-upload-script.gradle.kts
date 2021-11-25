import BuildConstants.curseforgeId
import BuildConstants.majorMinecraftVersion
import BuildConstants.minecraftVersion
import BuildConstants.modrinthId
import BuildConstants.projectState
import BuildConstants.projectStateType
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import com.modrinth.minotaur.TaskModrinthUpload
import com.modrinth.minotaur.request.Dependency

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")
    id("com.modrinth.minotaur")
}

val fabrikModules = listOf(
    project(":${rootProject.name}-commands"),
    project(":${rootProject.name}-compose"),
    project(":${rootProject.name}-core"),
    project(":${rootProject.name}-game"),
    project(":${rootProject.name}-igui"),
    project(":${rootProject.name}-nbt"),
    project(":${rootProject.name}-network"),
    project(":${rootProject.name}-persistence")
)

tasks {
    register<TaskModrinthUpload>("uploadModrinth") {
        group = "upload"
        onlyIf {
            findProperty("modrinth.token") != null
        }

        token = findProperty("modrinth.token").toString()

        projectId = modrinthId
        versionNumber = rootProject.version.toString()
        addGameVersion(minecraftVersion)
        addLoader("fabric")
        versionType = projectStateType

        uploadFile = remapJar.get()
        fabrikModules.forEach {
            addFile(it.tasks.named("remapJar").get())
        }

        addDependency("gjN9CB30", Dependency.DependencyType.REQUIRED)
        addDependency("1qsZV7U7", Dependency.DependencyType.REQUIRED)
    }

    named("curseforge") {
        onlyIf {
            findProperty("curseforge.token") != null
        }
        dependsOn(tasks.named("remapJar"))
    }
}

curseforge {
    apiKey = findProperty("curseforge.token").toString()

    project(closureOf<CurseProject> {
        mainArtifact(tasks.named("remapJar").get())

        id = curseforgeId
        releaseType = projectState
        addGameVersion(if (minecraftVersion.split('-').size <= 1) minecraftVersion else "$majorMinecraftVersion-Snapshot")

        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
        })
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
