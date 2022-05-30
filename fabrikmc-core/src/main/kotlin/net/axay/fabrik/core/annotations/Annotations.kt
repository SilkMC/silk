package net.axay.fabrik.core.annotations

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    message = "This is an internal fabrikmc API that " +
            "should not be used from the outside. No compatibility guarantees are provided. " +
            "It is recommended to report your use-case of internal API to the fabrikmc team.",
    level = RequiresOptIn.Level.ERROR
)
annotation class InternalFabrikApi

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    message = "This is a delicate API and its use requires care." +
            " Make sure you fully understand the declaration that is marked as a delicate API." +
            " It is likely that there is another better way to do what you are trying to do.",
    level = RequiresOptIn.Level.WARNING
)
annotation class DelicateFabrikApi
