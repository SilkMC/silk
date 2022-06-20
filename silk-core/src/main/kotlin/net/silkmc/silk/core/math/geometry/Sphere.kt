@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.core.math.geometry

import net.silkmc.silk.core.world.pos.Pos3i
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i

/**
 * Contains the core sphere generator functions.
 */
object Sphere {
    /**
     * Produces positions for a filled sphere around x=0, y=0, z=0.
     */
    inline fun produceFilledSpherePositions(radius: Int, consumer: (Pos3i) -> Unit) {
        for (xIter in (-radius)..(+radius))
            for (yIter in (-radius)..(+radius))
                for (zIter in (-radius)..(+radius))
                    if (
                        xIter * xIter +
                        yIter * yIter +
                        zIter * zIter
                        < radius * radius
                    ) consumer(Pos3i(xIter, yIter, zIter))
    }

    /**
     * Builds a set using [produceFilledSpherePositions].
     */
    fun filledSpherePositionSet(radius: Int) =
        HashSet<Pos3i>().apply {
            produceFilledSpherePositions(radius) { add(it) }
        }
}

/**
 * Produces positions for a filled sphere around this position.
 */
inline fun Vec3i.produceFilledSpherePositions(radius: Int, crossinline consumer: (BlockPos) -> Unit) {
    val x = this.x
    val y = this.y
    val z = this.z
    Sphere.produceFilledSpherePositions(radius) {
        consumer(BlockPos(x + it .x, y + it.y, z + it.z))
    }
}

/**
 * Builds a set using [produceFilledSpherePositions].
 */
fun Vec3i.filledSpherePositionSet(radius: Int) =
    HashSet<BlockPos>().apply {
        produceFilledSpherePositions(radius) { add(it) }
    }
