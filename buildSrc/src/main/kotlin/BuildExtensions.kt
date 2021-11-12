import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.modProject(path: String) =
    project(path, configuration = "namedElements")
