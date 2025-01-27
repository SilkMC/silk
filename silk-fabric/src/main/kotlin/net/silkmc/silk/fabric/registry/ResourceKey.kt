@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

fun <T> resourceKeyOf(resourceKey: ResourceKey<out Registry<T>>, resourceLocation: ResourceLocation): ResourceKey<T> {
    return ResourceKey.create(resourceKey, resourceLocation)
}

fun <T> registryKeyOf(resourceLocation: ResourceLocation): ResourceKey<Registry<T>> {
    return ResourceKey.createRegistryKey(resourceLocation)
}