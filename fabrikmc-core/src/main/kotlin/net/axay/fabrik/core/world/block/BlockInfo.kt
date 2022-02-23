package net.axay.fabrik.core.world.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

data class BlockInfo(
    val block: Block,
    val state: BlockState,
    val pos: BlockPos,
) {
    constructor(state: BlockState, pos: BlockPos) : this(state.block, state, pos)
}
