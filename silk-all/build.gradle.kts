import BuildConstants.projectTitle

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `mod-upload-script`
}

dependencies {
    BuildConstants.uploadModules.forEach {
        implementation(include(modProject(":${rootProject.name}-${it}"))!!)
    }
}

val modName by extra("$projectTitle (All modules)")
val isModParent by extra(true)
