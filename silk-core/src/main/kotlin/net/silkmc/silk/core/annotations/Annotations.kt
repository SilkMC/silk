package net.silkmc.silk.core.annotations

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@RequiresOptIn(
    message = "This is an internal Silk API that " +
            "should not be used from the outside. No compatibility guarantees are provided. " +
            "It is recommended to report your use-case of internal API to the Silk team.",
    level = RequiresOptIn.Level.ERROR
)
annotation class InternalSilkApi

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@RequiresOptIn(
    message = "This is a delicate API and its use requires care." +
            " Make sure you fully understand the declaration that is marked as a delicate API." +
            " It is likely that there is another better way to do what you are trying to do.",
    level = RequiresOptIn.Level.WARNING
)
annotation class DelicateSilkApi

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@RequiresOptIn(
    message = "This is an experimental API and its use requires care, since" +
        " stability cannot be guaranteed for this API yet." +
        " This means that the functionality of this API may not work correctly yet, or that" +
        " breaking changes can occur at any time.",
    level = RequiresOptIn.Level.WARNING
)
annotation class ExperimentalSilkApi
