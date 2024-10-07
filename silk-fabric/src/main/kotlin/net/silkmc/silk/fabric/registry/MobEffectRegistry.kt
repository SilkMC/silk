@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

fun <T : MobEffect> T.register(id: ResourceLocation): Holder.Reference<MobEffect> {
    return BuiltInRegistries.MOB_EFFECT.registerForHolder(id, this)
}

fun <T : MobEffect> T.register(id: String): Holder.Reference<MobEffect> {
    return BuiltInRegistries.MOB_EFFECT.registerForHolder(id, this)
}

fun <T : MobEffect> T.asHolder(): Holder<T> {
    return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this) as Holder<T>
}
