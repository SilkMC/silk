package net.axay.fabrik.core.world.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

data class BlockInfo(
    val block: Block,
    val state: BlockState,
    val pos: BlockPos,
) {
    constructor(state: BlockState, pos: BlockPos) : this(state.block, state, pos)
}
