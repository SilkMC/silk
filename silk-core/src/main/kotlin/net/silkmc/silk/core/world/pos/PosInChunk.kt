package net.silkmc.silk.core.world.pos

import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos

data class PosInChunk(val x: Int, val y: Int, val z: Int) {
    constructor(blockPos: BlockPos) : this(blockPos.x and 15, blockPos.y, blockPos.z and 15)

    fun getBlockPos(chunkPos: ChunkPos) = BlockPos(chunkPos.minBlockX + x, y, chunkPos.minBlockZ + z)
}
