@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

fun <T : CriterionTrigger<*>> T.register(id: ResourceLocation): T = BuiltInRegistries.TRIGGER_TYPES.register(id, this)
fun <T : CriterionTrigger<*>> T.register(id: String): T = BuiltInRegistries.TRIGGER_TYPES.register(id, this)

