import BuildConstants.minecraftVersion
import BuildConstants.projectState
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")
}

/**
 * PUBLISH
 */

tasks.withType<CurseUploadTask> {
    dependsOn(tasks.withType<RemapJarTask>())
}

curseforge {
    apiKey = property("curseforge.token") ?: ""
    project(closureOf<CurseProject> {
        id = "447425"

        releaseType = projectState
        addGameVersion(minecraftVersion)
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
