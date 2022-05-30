import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-upload-script`
}

dependencies {
    BuildConstants.uploadModules.forEach {
        include(modProject(":${rootProject.name}-${it}"))
    }
}

val modName by extra("$projectTitle (All modules)")
val isModParent by extra(true)
