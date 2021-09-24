@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.core.math.geometry

import net.axay.fabrik.core.world.pos.Pos3i
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

object Sphere {
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

    fun filledSpherePositionSet(radius: Int) =
        HashSet<Pos3i>().apply {
            produceFilledSpherePositions(radius) { add(it) }
        }
}

inline fun Vec3i.produceFilledSpherePositions(radius: Int, crossinline consumer: (BlockPos) -> Unit) {
    val x = this.x
    val y = this.y
    val z = this.z
    Sphere.produceFilledSpherePositions(radius) {
        consumer(BlockPos(x + it .x, y + it.y, z + it.z))
    }
}

fun Vec3i.filledSpherePositionSet(radius: Int) =
    HashSet<BlockPos>().apply {
        produceFilledSpherePositions(radius) { add(it) }
    }
