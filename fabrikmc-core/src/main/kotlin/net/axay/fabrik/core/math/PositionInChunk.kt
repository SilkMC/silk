package net.axay.fabrik.core.math

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

data class PositionInChunk(val x: Int, val y: Int, val z: Int) {
    constructor(blockPos: BlockPos) : this(blockPos.x and 15, blockPos.y, blockPos.z and 15)

    fun getBlockPos(chunkPos: ChunkPos) = BlockPos(chunkPos.startX + x, y, chunkPos.startZ + z)
}
