@file:Suppress("unused")

package net.silkmc.silk.core.world.pos

import com.mojang.math.Vector3d
import com.mojang.math.Vector3f
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import net.silkmc.silk.core.annotations.DelicateSilkApi

/**
 * A simple class representing **a pair of x and z** (being 2-dimensional **integer** coordinates).
 * As y would be the height in Minecraft, this two-dimensional class does not contain a y value.
 */
@Serializable
data class Pos2i(override val x: Int, override val z: Int) :
    Pos2Dimensional<Int>

/**
 * A simple class representing **a pair of x and z** (being 2-dimensional **floating point** coordinates).
 * As y would be the height in Minecraft, this two-dimensional class does not contain a y value.
 */
@Serializable
data class Pos2f(override val x: Float, override val z: Float) :
    Pos2Dimensional<Float>

/**
 * A simple class representing **a pair of x and z** (being 2-dimensional **double** coordinates).
 * As y would be the height in Minecraft, this two-dimensional class does not contain a y value.
 */
@Serializable
data class Pos2d(override val x: Double, override val z: Double) :
    Pos2Dimensional<Double>

@Serializable
sealed interface Pos2Dimensional<N : Number> {
    val x: N
    val z: N

    operator fun component1() = x
    operator fun component2() = z

    fun toPair() = Pair(x, z)

    fun toMcVec2() = Vec2(x.toFloat(), z.toFloat())

    /**
     * Keeps the x and z coordinate, but puts this position into 3-dimensional
     * space at the given height [y].
     */
    fun to3DimensionalAtY(y: N): Pos3Dimensional<N> {
        val threeDimensional =  when (this) {
            is Pos2i, is Pos3i -> Pos3i(x.toInt(), y.toInt(), z.toInt())
            is Pos2f, is Pos3f -> Pos3f(x.toFloat(), y.toFloat(), z.toFloat())
            is Pos2d, is Pos3d -> Pos3d(x.toDouble(), y.toDouble(), z.toDouble())
        }
        @Suppress("UNCHECKED_CAST")
        return threeDimensional as Pos3Dimensional<N>
    }

    @Deprecated(
        message = "Consistent conversion functions are now available, making this function useless.",
        replaceWith = ReplaceWith("this.to3DimensionalAtY(y).toMcBlockPos()")
    )
    fun toBlockPos(y: N) = BlockPos(x.toInt(), y.toInt(), z.toInt())
}

/**
 * A class representing **a triple of x, y and z** (being 3-dimensional **integer** coordinates),
 * where y is the height in Minecraft.
 */
@Serializable
data class Pos3i(override val x: Int, override val y: Int, override val z: Int) :
    Pos3Dimensional<Int>

/**
 * A class representing **a triple of x, y and z** (being 3-dimensional **floating point** coordinates),
 * where y is the height in Minecraft.
 */
@Serializable
data class Pos3f(override val x: Float, override val y: Float, override val z: Float) :
    Pos3Dimensional<Float>

/**
 * A class representing **a triple of x, y and z** (being 3-dimensional **double** coordinates),
 * where y is the height in Minecraft.
 */
@Serializable
data class Pos3d(override val x: Double, override val y: Double, override val z: Double) :
    Pos3Dimensional<Double>

@Serializable
sealed interface Pos3Dimensional<N : Number> : Pos2Dimensional<N> {
    override val x: N
    val y: N
    override val z: N

    override operator fun component1() = x
    override operator fun component2() = y
    operator fun component3() = z

    fun toTriple() = Triple(x, y, z)

    fun toMcVec3i() = Vec3i(x.toInt(), y.toInt(), z.toInt())
    fun toMcBlockPos() = BlockPos(x.toInt(), y.toInt(), z.toInt())

    fun toMcVector3f() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    fun toMcVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())
    fun toMcVector3d() = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

    fun atY(y: N) = to3DimensionalAtY(y)

    /**
     * Overrides [Pos2Dimensional.to3DimensionalAtY] and marks it as delicate api.
     * You probably want to use [atY] for 3-dimensional positions instead.
     */
    @DelicateSilkApi
    override fun to3DimensionalAtY(y: N) = super.to3DimensionalAtY(y)

    @Deprecated(
        message = "This function has been renamed to make it consistent with the other conversion functions.",
        replaceWith = ReplaceWith("this.toMcBlockPos()")
    )
    fun toBlockPos() = toMcBlockPos()
}
