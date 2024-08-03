package net.silkmc.silk.core.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

fun <T : MobEffect> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.MOB_EFFECT.register(id, this)
}

fun <T : MobEffect> T.register(id: String): T {
    return register(ResourceLocation.parse(id))
}
