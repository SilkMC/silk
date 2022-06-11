package net.axay.silk.core.world.pos

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i

/**
 * A simple class representing **a pair of x and z**. As y would be the height
 * in Minecraft, this two-dimensional class does not contain a y value.
 */
data class Pos2i(val x: Int, val z: Int) {
    fun toPair() = x to z
    fun toBlockPos(y: Int) = BlockPos(x, y, z)
}

/**
 * A class representing **a triple of x, y and z**, where y is the height in Minecraft.
 */
data class Pos3i(val x: Int, val y: Int, val z: Int) {
    fun toTriple() = Triple(x, y, z)
    fun toVec3i() = Vec3i(x, y, z)
    fun toBlockPos() = BlockPos(x, y, z)
}
