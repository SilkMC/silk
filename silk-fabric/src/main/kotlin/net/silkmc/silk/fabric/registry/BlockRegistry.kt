@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour


inline fun blockPropertiesOf(
    copiedBlock: BlockBehaviour? = null,
    builder: BlockBehaviour.Properties.() -> Unit,
): BlockBehaviour.Properties {
    return if (copiedBlock != null)
        BlockBehaviour.Properties.ofFullCopy(copiedBlock).apply(builder)
    else
        BlockBehaviour.Properties.of().apply(builder)
}

fun <T : Block> T.register(id: ResourceLocation): T {
    for (blockState in stateDefinition.possibleStates) {
        Block.BLOCK_STATE_REGISTRY.add(blockState)
        blockState.initCache()
    }

    return BuiltInRegistries.BLOCK.register(id, this)
}

fun <T : Block> T.register(id: String): T {
    return BuiltInRegistries.BLOCK.register(id, this)
}
