package net.silkmc.silk.core.registry

import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

fun <V : Any, T : V> Registry<V>.register(id: ResourceLocation, entry: T): T = Registry.register(this, id, entry)

fun <V : Any, T : V> Registry<in V>.register(id: String, entry: T): T = Registry.register(this, id, entry)

operator fun HolderSet<Block>.contains(state: BlockState): Boolean = state.`is`(this)

operator fun TagKey<Block>.contains(state: BlockState): Boolean = state.`is`(this)
