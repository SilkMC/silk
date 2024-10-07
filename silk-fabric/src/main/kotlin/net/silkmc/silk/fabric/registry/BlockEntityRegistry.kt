@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

fun <T : BlockEntity> blockEntityTypeOf(
    vararg blocks: Block,
    factory: BlockEntityType.BlockEntitySupplier<out T>,
): BlockEntityType<T> {
    return BlockEntityType.Builder.of(factory, *blocks).build()
}

fun <T : BlockEntityType<V>, V> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.BLOCK_ENTITY_TYPE.register(id, this)
}

fun <T : BlockEntityType<V>, V> T.register(id: String): T {
    return BuiltInRegistries.BLOCK_ENTITY_TYPE.register(id, this)
}

