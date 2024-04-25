package net.silkmc.silk.core.annotations

/**
 * Marks an element, implementation or similar to possibly be dependent
 * on a specific Minecraft version. This indicates, that the element
 * requires some extra care when updating Silk to a new version.
 */
@InternalSilkApi
@Retention(AnnotationRetention.SOURCE)
annotation class MinecraftVersionDependent(val lastChecked: String)
