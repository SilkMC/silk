@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.silkmc.silk.core.world.pos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import org.joml.Vector3f

/**
 * Base interface for position class implementations.
 */
@Serializable
sealed interface IPos<N : Number> {

    /**
     * Horizontal x-coordinate, equivalent of `x` in a Minecraft world.
     */
    val x: N

    /**
     * Horizontal z-coordinate, equivalent of `z` in a Minecraft world.
     * In general maths this would be the y coordinate on a regular 2d plane.
     */
    val z: N

    /**
     * Converts this position to a basic Kotlin [Pair] of [x] and [z].
     */
    fun toPair(): Pair<N, N> {
        return x to z
    }
}

@Serializable
sealed interface Pos2Dimensional<N : Number> : IPos<N> {

    fun toMcVec2() = Vec2(x.toFloat(), z.toFloat())

    operator fun component1() = x
    operator fun component2() = z

    @Deprecated(
        message = "Consistent conversion functions are now available, making this function useless.",
        replaceWith = ReplaceWith("this.to3DimensionalAtY(y).toMcBlockPos()")
    )
    fun toBlockPos(y: N) = BlockPos(x.toInt(), y.toInt(), z.toInt())
}

@Serializable
sealed interface Pos3Dimensional<N : Number> : IPos<N> {

    /**
     * Vertical y-coordinate, equivalent of `y` in a Minecraft world.
     * This coordinate represents the **height**.
     */
    val y: N

    /**
     * Converts this position to a Kotlin [Triple] of [x], [y] and [z].
     */
    fun toTriple() = Triple(x, y, z)

    fun toMcVec3i() = Vec3i(x.toInt(), y.toInt(), z.toInt())
    fun toMcBlockPos() = BlockPos(x.toInt(), y.toInt(), z.toInt())

    fun toMcVector3f() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    fun toMcVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())
    fun toMcVector3d() = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z

    @Deprecated(
        message = "This function has been renamed to make it consistent with the other conversion functions.",
        replaceWith = ReplaceWith("this.toMcBlockPos()")
    )
    fun toBlockPos() = toMcBlockPos()
}

@Serializable
sealed interface Pos2DimensionalConvertible<N : Number, In3d, Self> : Pos2Dimensional<N>
        where Self : Pos2DimensionalConvertible<N, In3d, Self>,
              In3d : Pos3DimensionalConvertible<N, Self, In3d> {

    /**
     * Keeps the [x] and [z] coordinate, but puts this position into 3-dimensional
     * space ([Pos3Dimensional]) at the given height [y].
     */
    fun atY(y: N): In3d
}

@Serializable
sealed interface Pos3DimensionalConvertible<N : Number, In2d, Self> : Pos3Dimensional<N>
        where Self : Pos3DimensionalConvertible<N, In2d, Self>,
              In2d : Pos2DimensionalConvertible<N, Self, In2d> {

    /**
     * Removes the [y] coordinate and returns a new instance of [Pos2Dimensional].
     */
    fun withoutHeight(): In2d

    /**
     * Takes the [x] and [z] coordinate and adds them together
     * with the given new [y].
     */
    fun atY(y: N): Self
}

/**
 * A simple class representing **a pair of x and z** (being 2-dimensional **integer** coordinates).
 * As y would be the height in Minecraft, this two-dimensional class does not contain a y value.
 */
@Serializable
@SerialName("pos2i")
data class Pos2i(override val x: Int, override val z: Int) : Pos2DimensionalConvertible<Int, Pos3i, Pos2i> {
    override fun atY(y: Int) = Pos3i(x, y, z)
}

/**
 * A simple class representing **a pair of x and z** (being 2-dimensional **floating point** coordinates).
 * As y would be the height in Minecraft, this two-dimensional class does not contain a y value.
 */
@Serializable
@SerialName("pos2f")
data class Pos2f(override val x: Float, override val z: Float) : Pos2DimensionalConvertible<Float, Pos3f, Pos2f> {
    override fun atY(y: Float) = Pos3f(x, y, z)
}

/**
 * A simple class representing **a pair of x and z** (being 2-dimensional **double** coordinates).
 * As y would be the height in Minecraft, this two-dimensional class does not contain a y value.
 */
@Serializable
@SerialName("pos2d")
data class Pos2d(override val x: Double, override val z: Double) : Pos2DimensionalConvertible<Double, Pos3d, Pos2d> {
    override fun atY(y: Double) = Pos3d(x, y, z)
}

/**
 * A class representing **a triple of x, y and z** (being 3-dimensional **integer** coordinates),
 * where y is the height in Minecraft.
 */
@Serializable
@SerialName("pos3i")
data class Pos3i(override val x: Int, override val y: Int, override val z: Int) : Pos3DimensionalConvertible<Int, Pos2i, Pos3i> {
    override fun atY(y: Int) = Pos3i(x, y, z)
    override fun withoutHeight() = Pos2i(x, z)
}

/**
 * A class representing **a triple of x, y and z** (being 3-dimensional **floating point** coordinates),
 * where y is the height in Minecraft.
 */
@Serializable
@SerialName("pos3f")
data class Pos3f(override val x: Float, override val y: Float, override val z: Float) : Pos3DimensionalConvertible<Float, Pos2f, Pos3f> {
    override fun atY(y: Float) = Pos3f(x, y, z)
    override fun withoutHeight() = Pos2f(x, z)
}

/**
 * A class representing **a triple of x, y and z** (being 3-dimensional **double** coordinates),
 * where y is the height in Minecraft.
 */
@Serializable
@SerialName("pos3d")
data class Pos3d(override val x: Double, override val y: Double, override val z: Double) : Pos3DimensionalConvertible<Double, Pos2d, Pos3d> {
    override fun atY(y: Double) = Pos3d(x, y, z)
    override fun withoutHeight() = Pos2d(x, z)
}
