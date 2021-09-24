@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.core.math.geometry

import net.axay.fabrik.core.world.pos.Pos2i
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

object Circle {
    inline fun produceCirclePositions(radius: Int, crossinline consumer: (Pos2i) -> Unit) {
        val addLoc: (Int, Int) -> Unit = { first, second ->
            consumer(Pos2i(first, second))
        }

        var d = -radius
        var x = radius
        var y = 0

        while (y <= x) {
            addLoc(x, y)
            addLoc(x, -y)
            addLoc(-x, y)
            addLoc(-x, -y)
            addLoc(y, x)
            addLoc(y, -x)
            addLoc(-y, x)
            addLoc(-y, -x)

            d += 2 * y + 1
            y++

            if (d > 0) {
                d += -2 * x + 2
                x--
            }
        }
    }

    inline fun produceFilledCirclePositions(radius: Int, consumer: (Pos2i) -> Unit) {
        for (xIter in (-radius)..(+radius))
            for (zIter in (-radius)..(+radius))
                if (
                    xIter * xIter +
                    zIter * zIter
                    < radius * radius
                ) consumer(Pos2i(xIter, zIter))
    }

    fun circlePositionSet(radius: Int) =
        HashSet<Pos2i>().apply {
            produceCirclePositions(radius) { add(it) }
        }

    fun filledCirclePositionSet(radius: Int) =
        HashSet<Pos2i>().apply {
            produceFilledCirclePositions(radius) { add(it) }
        }
}

inline fun Vec3i.produceCirclePositions(radius: Int, crossinline consumer: (BlockPos) -> Unit) {
    val x = this.x
    val y = this.y
    val z = this.z
    Circle.produceCirclePositions(radius) {
        consumer(BlockPos(x + it.x, y, z + it.z))
    }
}

inline fun Vec3i.produceFilledCirclePositions(radius: Int, crossinline consumer: (BlockPos) -> Unit) {
    val x = this.x
    val y = this.y
    val z = this.z
    Circle.produceFilledCirclePositions(radius) {
        consumer(BlockPos(x + it.x, y, z + it.z))
    }
}

fun Vec3i.circlePositionSet(radius: Int) =
    HashSet<BlockPos>().apply {
        produceCirclePositions(radius) { add(it) }
    }

fun Vec3i.filledCirclePositionSet(radius: Int) =
    HashSet<BlockPos>().apply {
        produceFilledCirclePositions(radius) { add(it) }
    }
