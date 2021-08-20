import BuildConstants.curseforgeId
import BuildConstants.fabrikVersion
import BuildConstants.minecraftVersion
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

tasks {
    val modrinthTask = register<TaskModrinthUpload>("uploadModrinth") {
        group = "upload"
        token = findProperty("modrinth.token").toString()
        projectId = "aTaCgKLW"
        versionNumber = fabrikVersion
        uploadFile = remapJar
        addGameVersion(minecraftVersion)
        addLoader("fabric")
        versionType = projectStateType
    }

    register("publishAndUploadMod") {
        group = "upload"
        dependsOn(curseforge)
        dependsOn(modrinthTask)
        dependsOn(tasks.named("publish"))
    }
}

curseforge {
    apiKey = findProperty("curseforge.token") ?: ""
    project(closureOf<CurseProject> {
        mainArtifact(tasks.getByName("remapJar").outputs.files.first())

        id = curseforgeId
        releaseType = projectState
        addGameVersion(minecraftVersion)

        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
        })

        afterEvaluate {
            mainArtifact(tasks.named("remapJar"))
            uploadTask.dependsOn(tasks.named("remapJar"))
        }
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
