@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion

fun potionOf(
    potionName: String? = null,
    effect: Holder<MobEffect>,
    duration: Int = 0,
    amplifier: Int = 0,
    ambient: Boolean = false,
    showParticles: Boolean = true,
    showIcon: Boolean = showParticles,
): Potion {
    val effectInstance = MobEffectInstance(effect, duration, amplifier, ambient, showParticles, showIcon)
    return Potion(potionName, effectInstance)
}

fun potionOf(
    potionName: String? = null,
    vararg effectInstances: MobEffectInstance,
): Potion {
    return Potion(potionName, *effectInstances)
}

fun potionOf(
    potionName: String? = null,
    effectsBuilder: MutableList<MobEffectInstance>.() -> Unit,
): Potion {
    return Potion(potionName, *buildList(effectsBuilder).toTypedArray())
}

fun Potion.register(id: ResourceLocation): Potion {
    return BuiltInRegistries.POTION.register(id, this)
}

fun Potion.register(id: String): Potion {
    return BuiltInRegistries.POTION.register(id, this)
}

