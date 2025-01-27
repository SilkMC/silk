import BuildConstants.projectTitle

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `mod-upload-script`
}

dependencies {
    BuildConstants.commonModules.forEach {
        implementation(include(project(":${rootProject.name}-${it}"))!!)
    }
    implementation(include(modProject(":${rootProject.name}-fabric"))!!)
}

val modName by extra("$projectTitle (All modules)")
val isModParent by extra(true)
