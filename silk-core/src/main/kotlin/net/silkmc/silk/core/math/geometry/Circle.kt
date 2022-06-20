@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.core.math.geometry

import net.silkmc.silk.core.world.pos.Pos2i
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i

/**
 * Contains the core circle generator functions.
 */
object Circle {
    /**
     * Produces positions for a hollow circle around x=0, z=0.
     */
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

    /**
     * Produces positions for a filled circle around x=0, z=0.
     */
    inline fun produceFilledCirclePositions(radius: Int, consumer: (Pos2i) -> Unit) {
        for (xIter in (-radius)..(+radius))
            for (zIter in (-radius)..(+radius))
                if (
                    xIter * xIter +
                    zIter * zIter
                    < radius * radius
                ) consumer(Pos2i(xIter, zIter))
    }

    /**
     * Builds a set using [produceCirclePositions].
     */
    fun circlePositionSet(radius: Int) =
        HashSet<Pos2i>().apply {
            produceCirclePositions(radius) { add(it) }
        }

    /**
     * Builds a set using [produceFilledCirclePositions].
     */
    fun filledCirclePositionSet(radius: Int) =
        HashSet<Pos2i>().apply {
            produceFilledCirclePositions(radius) { add(it) }
        }
}

/**
 * Produces positions for a hollow circle around this position.
 */
inline fun Vec3i.produceCirclePositions(radius: Int, crossinline consumer: (BlockPos) -> Unit) {
    val x = this.x
    val y = this.y
    val z = this.z
    Circle.produceCirclePositions(radius) {
        consumer(BlockPos(x + it.x, y, z + it.z))
    }
}

/**
 * Produces positions for a filled circle around this position.
 */
inline fun Vec3i.produceFilledCirclePositions(radius: Int, crossinline consumer: (BlockPos) -> Unit) {
    val x = this.x
    val y = this.y
    val z = this.z
    Circle.produceFilledCirclePositions(radius) {
        consumer(BlockPos(x + it.x, y, z + it.z))
    }
}

/**
 * Builds a set using [produceCirclePositions].
 */
fun Vec3i.circlePositionSet(radius: Int) =
    HashSet<BlockPos>().apply {
        produceCirclePositions(radius) { add(it) }
    }

/**
 * Builds a set using [produceFilledCirclePositions].
 */
fun Vec3i.filledCirclePositionSet(radius: Int) =
    HashSet<BlockPos>().apply {
        produceFilledCirclePositions(radius) { add(it) }
    }
