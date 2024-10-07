@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid

fun <T : Fluid> T.register(id: ResourceLocation): T = BuiltInRegistries.FLUID.register(id, this)
fun <T : Fluid> T.register(id: String): T = BuiltInRegistries.FLUID.register(id, this)
