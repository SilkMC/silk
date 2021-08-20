import BuildConstants.curseforgeId
import BuildConstants.fabrikVersion
import BuildConstants.minecraftVersion
import BuildConstants.modrinthId
import BuildConstants.projectState
import BuildConstants.projectStateType
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import com.modrinth.minotaur.TaskModrinthUpload

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")
    id("com.modrinth.minotaur")
}

val fabrikModules = listOf(
    project(":${rootProject.name}-commands"),
    project(":${rootProject.name}-core"),
    project(":${rootProject.name}-igui"),
    project(":${rootProject.name}-nbt"),
    project(":${rootProject.name}-persistence")
)

tasks {
    val modrinthTask = register<TaskModrinthUpload>("uploadModrinth") {
        group = "upload"
        onlyIf {
            findProperty("modrinth.token") != null
        }

        token = findProperty("modrinth.token").toString()

        projectId = modrinthId
        versionNumber = fabrikVersion
        addGameVersion(minecraftVersion)
        addLoader("fabric")
        versionType = projectStateType

        uploadFile = remapJar.get()
        fabrikModules.forEach {
            addFile(it.tasks.named("remapJar").get())
        }
    }

    register("publishAndUploadMod") {
        group = "upload"
        dependsOn(curseforge)
        dependsOn(modrinthTask)
        dependsOn(tasks.named("publish"))
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
        addGameVersion(minecraftVersion)

        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
        })
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
