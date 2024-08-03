@file:Suppress("unused")

package net.silkmc.silk.core.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour


inline fun blockProperties(
    copiedBlock: BlockBehaviour,
    builder: BlockBehaviour.Properties.() -> Unit,
): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(copiedBlock).apply(builder)

// alias for yarn
inline fun blockSettings(
    copiedBlock: BlockBehaviour,
    builder: BlockBehaviour.Properties.() -> Unit,
): BlockBehaviour.Properties = blockProperties(copiedBlock, builder)

inline fun blockProperties(
    builder: BlockBehaviour.Properties.() -> Unit,
): BlockBehaviour.Properties = BlockBehaviour.Properties.of().apply(builder)

// alias for yarn
inline fun blockSettings(
    builder: BlockBehaviour.Properties.() -> Unit,
): BlockBehaviour.Properties = blockProperties(builder)

fun <T : Block> T.register(id: ResourceLocation): T {
    for (blockState in stateDefinition.possibleStates) {
        Block.BLOCK_STATE_REGISTRY.add(blockState)
        blockState.initCache()
    }

    return BuiltInRegistries.BLOCK.register(id, this)
}

fun <T : Block> T.register(id: String): T {
    return register(ResourceLocation.parse(id))
}
