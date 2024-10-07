@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

fun <V : Any, T : V> Registry<V>.register(id: ResourceLocation, entry: T): T {
    return Registry.register(this, id, entry)
}

fun <V : Any, T : V> Registry<V>.register(id: String, entry: T): T {
    return Registry.register(this, ResourceLocation.parse(id), entry)
}

fun <V : Any, T : V> Registry<V>.register(key: ResourceKey<V>, entry: T): T {
    return Registry.register(this, key, entry)
}

fun <V : Any, T : V> Registry<V>.registerForHolder(id: ResourceLocation, entry: T): Holder.Reference<V> {
    return Registry.registerForHolder(this, id, entry)
}

fun <V : Any, T : V> Registry<V>.registerForHolder(id: String, entry: T): Holder.Reference<V> {
    return Registry.registerForHolder(this, ResourceLocation.parse(id), entry)
}

fun <V : Any, T : V> Registry<V>.registerForHolder(key: ResourceKey<V>, entry: T): Holder.Reference<V> {
    return Registry.registerForHolder(this, key, entry)
}

operator fun HolderSet<Block>.contains(state: BlockState): Boolean = state.`is`(this)

operator fun TagKey<Block>.contains(state: BlockState): Boolean = state.`is`(this)
