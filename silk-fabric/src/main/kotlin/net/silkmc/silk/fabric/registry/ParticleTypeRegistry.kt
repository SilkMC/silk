@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

fun <T : ParticleType<*>> T.register(id: ResourceLocation): T = BuiltInRegistries.PARTICLE_TYPE.register(id, this)
fun <T : ParticleType<*>> T.register(id: String): T = BuiltInRegistries.PARTICLE_TYPE.register(id, this)
