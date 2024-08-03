@file:Suppress("unused", "EXTENSION_SHADOWED_BY_MEMBER")

package net.silkmc.silk.core.math.vector

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt


// Vector initialization

fun vec3iOf(x: Int, y: Int, z: Int): Vec3i = Vec3i(x, y, z)
fun vec3iOf(vec: Vec3): Vec3i = Vec3i(vec.x.roundToInt(), vec.y.roundToInt(), vec.z.roundToInt())
fun vec3iOf(vec: Vector3i): Vec3i = Vec3i(vec.x, vec.y, vec.z)
fun vec3iOf(vec: Vector3f): Vec3i = Vec3i(vec.x.roundToInt(), vec.y.roundToInt(), vec.z.roundToInt())
fun vec3iOf(vec: Vector3d): Vec3i = Vec3i(vec.x.roundToInt(), vec.y.roundToInt(), vec.z.roundToInt())

fun vec3Of(x: Double, y: Double, z: Double): Vec3 = Vec3(x, y, z)
fun vec3Of(vec: Vec3i): Vec3 = Vec3(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
fun vec3Of(vec: Vector3i): Vec3 = Vec3(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
fun vec3Of(vec: Vector3f): Vec3 = Vec3(vec)
fun vec3Of(vec: Vector3d): Vec3 = Vec3(vec.x, vec.y, vec.z)

// for yarn mappings (Vec3 is Vec3d in yarn)
fun vec3dOf(x: Double, y: Double, z: Double): Vec3 = vec3Of(x, y, z)
fun vec3dOf(vec: Vec3i): Vec3 = Vec3(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
fun vec3dOf(vec: Vector3i): Vec3 = Vec3(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
fun vec3dOf(vec: Vector3f): Vec3 = Vec3(vec)
fun vec3dOf(vec: Vector3d): Vec3 = Vec3(vec.x, vec.y, vec.z)

fun vector3iOf(x: Int, y: Int, z: Int): Vector3i = Vector3i(x, y, z)
fun vector3iOf(vec: Vec3i): Vector3i = Vector3i(vec.x, vec.y, vec.z)
fun vector3iOf(vec: Vec3): Vector3i = Vector3i(vec.x.roundToInt(), vec.y.roundToInt(), vec.z.roundToInt())
fun vector3iOf(vec: Vector3i): Vector3i = Vector3i(vec) // copy constructor
fun vector3iOf(vec: Vector3f): Vector3i = Vector3i(vec.x.roundToInt(), vec.y.roundToInt(), vec.z.roundToInt())
fun vector3iOf(vec: Vector3d): Vector3i = Vector3i(vec.x.roundToInt(), vec.y.roundToInt(), vec.z.roundToInt())

fun vector3fOf(x: Float, y: Float, z: Float): Vector3f = Vector3f(x, y, z)
fun vector3fOf(vec: Vec3i): Vector3f = Vector3f(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
fun vector3fOf(vec: Vec3): Vector3f = Vector3f(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
fun vector3fOf(vec: Vector3i): Vector3f = Vector3f(vec)
fun vector3fOf(vec: Vector3f): Vector3f = Vector3f(vec) // copy constructor
fun vector3fOf(vec: Vector3d): Vector3f = Vector3f().set(vec)

fun vector3dOf(x: Double, y: Double, z: Double): Vector3d = Vector3d(x, y, z)
fun vector3dOf(vec: Vec3i): Vector3d = Vector3d(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
fun vector3dOf(vec: Vec3): Vector3d = Vector3d(vec.x, vec.y, vec.z)
fun vector3dOf(vec: Vector3i): Vector3d = Vector3d(vec)
fun vector3dOf(vec: Vector3f): Vector3d = Vector3d(vec)
fun vector3dOf(vec: Vector3d): Vector3d = Vector3d(vec) // copy constructor


fun vec2Of(x: Float, y: Float): Vec2 = Vec2(x, y)
fun vec2dOf(x: Float, y: Float): Vec2 = vec2Of(x, y) // for yarn mappings (Vec2 is Vec2d in yarn)

fun vector2iOf(x: Int, y: Int): Vector2i = Vector2i(x, y)
fun vector2iOf(vec: Vec2): Vector2i = Vector2i(vec.x.roundToInt(), vec.y.roundToInt())
fun vector2iOf(vec: Vector2i): Vector2i = Vector2i(vec) // copy constructor
fun vector2iOf(vec: Vector2f): Vector2i = Vector2i(vec.x.roundToInt(), vec.y.roundToInt())
fun vector2iOf(vec: Vector2d): Vector2i = Vector2i(vec.x.roundToInt(), vec.y.roundToInt())

fun vector2fOf(x: Float, y: Float): Vector2f = Vector2f(x, y)
fun vector2fOf(vec: Vec2): Vector2f = Vector2f(vec.x, vec.y)
fun vector2fOf(vec: Vector2i): Vector2f = Vector2f(vec)
fun vector2fOf(vec: Vector2f): Vector2f = Vector2f(vec) // copy constructor
fun vector2fOf(vec: Vector2d): Vector2f = Vector2f().set(vec)


fun vector2dOf(x: Double, y: Double): Vector2d = Vector2d(x, y)
fun vector2dOf(vec: Vec2): Vector2d = Vector2d(vec.x.toDouble(), vec.y.toDouble())
fun vector2dOf(vec: Vector2i): Vector2d = Vector2d(vec)
fun vector2dOf(vec: Vector2f): Vector2d = Vector2d(vec)
fun vector2dOf(vec: Vector2d): Vector2d = Vector2d(vec) // copy constructor


// Vec3 operations

operator fun Vec3.unaryMinus(): Vec3 = reverse()

operator fun Vec3.not(): Vec3 = reverse()

operator fun Vec3.plus(n: Int): Vec3 = n.toDouble().let { nDouble -> add(nDouble, nDouble, nDouble) }
operator fun Vec3.plus(n: Long): Vec3 = n.toDouble().let { nDouble -> add(nDouble, nDouble, nDouble) }
operator fun Vec3.plus(n: Float): Vec3 = n.toDouble().let { nDouble -> add(nDouble, nDouble, nDouble) }
operator fun Vec3.plus(n: Double): Vec3 = n.let { nDouble -> add(nDouble, nDouble, nDouble) }
operator fun Vec3.plus(n: Number): Vec3 = n.toDouble().let { nDouble -> add(nDouble, nDouble, nDouble) }

operator fun Vec3.minus(n: Int): Vec3 = n.toDouble().let { nDouble -> subtract(nDouble, nDouble, nDouble) }
operator fun Vec3.minus(n: Long): Vec3 = n.toDouble().let { nDouble -> subtract(nDouble, nDouble, nDouble) }
operator fun Vec3.minus(n: Float): Vec3 = n.toDouble().let { nDouble -> subtract(nDouble, nDouble, nDouble) }
operator fun Vec3.minus(n: Double): Vec3 = n.let { nDouble -> subtract(nDouble, nDouble, nDouble) }
operator fun Vec3.minus(n: Number): Vec3 = n.toDouble().let { nDouble -> subtract(nDouble, nDouble, nDouble) }

operator fun Vec3.times(n: Int): Vec3 = scale(n.toDouble())
operator fun Vec3.times(n: Long): Vec3 = scale(n.toDouble())
operator fun Vec3.times(n: Float): Vec3 = scale(n.toDouble())
operator fun Vec3.times(n: Double): Vec3 = scale(n)
operator fun Vec3.times(n: Number): Vec3 = scale(n.toDouble())

operator fun Vec3.div(n: Int): Vec3 = scale(1.0 / n.toDouble())
operator fun Vec3.div(n: Long): Vec3 = scale(1.0 / n.toDouble())
operator fun Vec3.div(n: Float): Vec3 = scale(1.0 / n.toDouble())
operator fun Vec3.div(n: Double): Vec3 = scale(1.0 / n)
operator fun Vec3.div(n: Number): Vec3 = scale(1.0 / n.toDouble())

operator fun Vec3.compareTo(n: Int) = length().compareTo(n)
operator fun Vec3.compareTo(n: Long) = length().compareTo(n)
operator fun Vec3.compareTo(n: Float) = length().compareTo(n)
operator fun Vec3.compareTo(n: Double) = length().compareTo(n)
operator fun Vec3.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vec3.plus(vec: Vec3): Vec3 = add(vec)

operator fun Vec3.minus(vec: Vec3): Vec3 = subtract(vec)

operator fun Vec3.times(vec: Vec3): Vec3 = multiply(vec)

operator fun Vec3.div(vec: Vec3): Vec3 = Vec3(x / vec.x, y / vec.y, z / vec.z)

infix fun Vec3.cross(vec: Vec3): Vec3 = cross(vec)

infix fun Vec3.dot(vec: Vec3): Double = dot(vec)

@Suppress("DeprecatedCallableAddReplaceWith") // a "replace with" quick-fix could be confusing here
@Deprecated(message = "This comparison is unintuitive and should be avoided")
operator fun Vec3.compareTo(vec: Vec3) = lengthSqr().compareTo(vec.lengthSqr())

operator fun Vec3.component1() = x
operator fun Vec3.component2() = y
operator fun Vec3.component3() = z


// Vec3i/BlockPos operations

inline operator fun <reified T : Vec3i> T.unaryMinus(): T = times(-1)

inline operator fun <reified T : Vec3i> T.not(): T = times(-1)

inline operator fun <reified T : Vec3i> T.plus(n: Int): T = n.let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.plus(n: Long): T = n.toInt().let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.plus(n: Float): T = n.toInt().let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.plus(n: Double): T = n.toInt().let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.plus(n: Number): T = n.toInt().let { nInt -> offset(nInt, nInt, nInt) } as T

inline operator fun <reified T : Vec3i> T.minus(n: Int): T = (-n).let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.minus(n: Long): T = (-n.toInt()).let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.minus(n: Float): T = (-n.toInt()).let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.minus(n: Double): T = (-n.toInt()).let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.minus(n: Number): T = (-n.toInt()).let { nInt -> offset(nInt, nInt, nInt) } as T

inline operator fun <reified T : Vec3i> T.times(n: Int): T = multiply(n) as T
inline operator fun <reified T : Vec3i> T.times(n: Long): T = multiply(n.toInt()) as T
operator fun Vec3i.times(n: Float): Vec3i = Vec3i((x * n).toInt(), (y * n).toInt(), (z * n).toInt())
operator fun Vec3i.times(n: Double): Vec3i = Vec3i((x * n).toInt(), (y * n).toInt(), (z * n).toInt())
operator fun Vec3i.times(n: Number): Vec3i =
    n.toDouble().let { nDouble -> Vec3i((x * nDouble).toInt(), (y * nDouble).toInt(), (z * nDouble).toInt()) }

operator fun BlockPos.times(n: Float): BlockPos = BlockPos((x * n).toInt(), (y * n).toInt(), (z * n).toInt())
operator fun BlockPos.times(n: Double): BlockPos = BlockPos((x * n).toInt(), (y * n).toInt(), (z * n).toInt())
operator fun BlockPos.times(n: Number): BlockPos =
    n.toDouble().let { nDouble -> BlockPos((x * nDouble).toInt(), (y * nDouble).toInt(), (z * nDouble).toInt()) }

operator fun Vec3i.div(n: Int): Vec3i = times(1.0 / n.toDouble())
operator fun Vec3i.div(n: Long): Vec3i = times(1.0 / n.toDouble())
operator fun Vec3i.div(n: Float): Vec3i = times(1.0 / n.toDouble())
operator fun Vec3i.div(n: Double): Vec3i = times(1.0 / n)
operator fun Vec3i.div(n: Number): Vec3i = times(1.0 / n.toDouble())

operator fun BlockPos.div(n: Int): BlockPos = times(1.0 / n.toDouble())
operator fun BlockPos.div(n: Long): BlockPos = times(1.0 / n.toDouble())
operator fun BlockPos.div(n: Float): BlockPos = times(1.0 / n.toDouble())
operator fun BlockPos.div(n: Double): BlockPos = times(1.0 / n)
operator fun BlockPos.div(n: Number): BlockPos = times(1.0 / n.toDouble())

operator fun Vec3i.compareTo(n: Int) = vec3Of(this).length().compareTo(n)
operator fun Vec3i.compareTo(n: Long) = vec3Of(this).length().compareTo(n)
operator fun Vec3i.compareTo(n: Float) = vec3Of(this).length().compareTo(n)
operator fun Vec3i.compareTo(n: Double) = vec3Of(this).length().compareTo(n)
operator fun Vec3i.compareTo(n: Number) = vec3Of(this).length().compareTo(n.toDouble())

inline operator fun <reified T : Vec3i> T.plus(vec: Vec3i): T = offset(vec) as T

inline operator fun <reified T : Vec3i> T.minus(vec: Vec3i): T = offset(!vec) as T

operator fun Vec3i.times(vec: Vec3i): Vec3i = Vec3i(x * vec.x, y * vec.y, z * vec.z)
operator fun BlockPos.times(vec: Vec3i): BlockPos = BlockPos(x * vec.x, y * vec.y, z * vec.z)

operator fun Vec3i.div(vec: Vec3i): Vec3i = Vec3i(x / vec.x, y / vec.y, z / vec.z)
operator fun BlockPos.div(vec: Vec3i): BlockPos = BlockPos(x / vec.x, y / vec.y, z / vec.z)

inline infix fun <reified T : Vec3i> T.cross(vec: Vec3i): T = cross(vec) as T

inline infix fun <reified T : Vec3i> T.dot(vec: Vec3i): Double = ((x * vec.x) + (y * vec.y) + (z * vec.z)).toDouble()

// there is an internal compareTo function
// operator fun Vec3i.compareTo(vec: Vec3i) = ...

operator fun Vec3i.component1() = x
operator fun Vec3i.component2() = y
operator fun Vec3i.component3() = z


// Vector3i operations

operator fun Vector3i.unaryMinus(): Vector3i = Vector3i(this).mul(-1)

operator fun Vector3i.not(): Vector3i = Vector3i(this).mul(-1)

operator fun Vector3i.plus(n: Int): Vector3i = n.let { nInt -> Vector3i(this).add(nInt, nInt, nInt) }
operator fun Vector3i.plus(n: Long): Vector3i = n.toInt().let { nInt -> Vector3i(this).add(nInt, nInt, nInt) }
operator fun Vector3i.plus(n: Float): Vector3i = n.toInt().let { nInt -> Vector3i(this).add(nInt, nInt, nInt) }
operator fun Vector3i.plus(n: Double): Vector3i = n.toInt().let { nInt -> Vector3i(this).add(nInt, nInt, nInt) }
operator fun Vector3i.plus(n: Number): Vector3i = n.toInt().let { nInt -> Vector3i(this).add(nInt, nInt, nInt) }

operator fun Vector3i.minus(n: Int): Vector3i = n.let { nInt -> Vector3i(this).sub(nInt, nInt, nInt) }
operator fun Vector3i.minus(n: Long): Vector3i = n.toInt().let { nInt -> Vector3i(this).sub(nInt, nInt, nInt) }
operator fun Vector3i.minus(n: Float): Vector3i = n.toInt().let { nInt -> Vector3i(this).sub(nInt, nInt, nInt) }
operator fun Vector3i.minus(n: Double): Vector3i = n.toInt().let { nInt -> Vector3i(this).sub(nInt, nInt, nInt) }
operator fun Vector3i.minus(n: Number): Vector3i = n.toInt().let { nInt -> Vector3i(this).sub(nInt, nInt, nInt) }

operator fun Vector3i.times(n: Int): Vector3i = Vector3i(this).mul(n)
operator fun Vector3i.times(n: Long): Vector3i = Vector3i(this).mul(n.toInt())
operator fun Vector3i.times(n: Float): Vector3i = Vector3i((x * n).toInt(), (y * n).toInt(), (z * n).toInt())
operator fun Vector3i.times(n: Double): Vector3i = Vector3i((x * n).toInt(), (y * n).toInt(), (z * n).toInt())
operator fun Vector3i.times(n: Number): Vector3i =
    n.toDouble().let { nDouble -> Vector3i((x * nDouble).toInt(), (y * nDouble).toInt(), (z * nDouble).toInt()) }

operator fun Vector3i.div(n: Int): Vector3i = Vector3i(this).div(n)
operator fun Vector3i.div(n: Long): Vector3i = Vector3i(this).div(n.toInt())
operator fun Vector3i.div(n: Float): Vector3i = Vector3i(this).div(n)
operator fun Vector3i.div(n: Double): Vector3i = Vector3i(this).div(n.toFloat())
operator fun Vector3i.div(n: Number): Vector3i = Vector3i(this).div(n.toFloat())

operator fun Vector3i.compareTo(n: Int) = length().compareTo(n)
operator fun Vector3i.compareTo(n: Long) = length().compareTo(n)
operator fun Vector3i.compareTo(n: Float) = length().compareTo(n)
operator fun Vector3i.compareTo(n: Double) = length().compareTo(n)
operator fun Vector3i.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vector3i.plus(vec: Vector3i): Vector3i = Vector3i(this).add(vec)

operator fun Vector3i.minus(vec: Vector3i): Vector3i = Vector3i(this).sub(vec)

operator fun Vector3i.times(vec: Vector3i): Vector3i = Vector3i(this).mul(vec)

operator fun Vector3i.div(vec: Vector3i): Vector3i = Vector3i(x / vec.x, y / vec.y, z / vec.z)

infix fun Vector3i.cross(vec: Vector3i): Vector3i = Vector3i(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

infix fun Vector3i.dot(vec: Vector3i): Double = ((x * vec.x) + (y * vec.y) + (z * vec.z)).toDouble()

operator fun Vector3i.component1() = x
operator fun Vector3i.component2() = y
operator fun Vector3i.component3() = z


// Vector3f operations

operator fun Vector3f.unaryMinus(): Vector3f = Vector3f(this).mul(-1.0f)

operator fun Vector3f.not(): Vector3f = Vector3f(this).mul(-1.0f)

operator fun Vector3f.plus(n: Int): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).add(nFloat, nFloat, nFloat) }
operator fun Vector3f.plus(n: Long): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).add(nFloat, nFloat, nFloat) }
operator fun Vector3f.plus(n: Float): Vector3f = Vector3f(this).add(n, n, n)
operator fun Vector3f.plus(n: Double): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).add(nFloat, nFloat, nFloat) }
operator fun Vector3f.plus(n: Number): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).add(nFloat, nFloat, nFloat) }

operator fun Vector3f.minus(n: Int): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).sub(nFloat, nFloat, nFloat) }
operator fun Vector3f.minus(n: Long): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).sub(nFloat, nFloat, nFloat) }
operator fun Vector3f.minus(n: Float): Vector3f = Vector3f(this).sub(n, n, n)
operator fun Vector3f.minus(n: Double): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).sub(nFloat, nFloat, nFloat) }
operator fun Vector3f.minus(n: Number): Vector3f = n.toFloat().let { nFloat -> Vector3f(this).sub(nFloat, nFloat, nFloat) }

operator fun Vector3f.times(n: Int): Vector3f = Vector3f(this).mul(n.toFloat())
operator fun Vector3f.times(n: Long): Vector3f = Vector3f(this).mul(n.toFloat())
operator fun Vector3f.times(n: Float): Vector3f = Vector3f(this).mul(n)
operator fun Vector3f.times(n: Double): Vector3f = Vector3f(this).mul(n.toFloat())
operator fun Vector3f.times(n: Number): Vector3f = Vector3f(this).mul(n.toFloat())

operator fun Vector3f.div(n: Int): Vector3f = Vector3f(this).div(n.toFloat())
operator fun Vector3f.div(n: Long): Vector3f = Vector3f(this).div(n.toFloat())
operator fun Vector3f.div(n: Float): Vector3f = Vector3f(this).div(n)
operator fun Vector3f.div(n: Double): Vector3f = Vector3f(this).div(n.toFloat())
operator fun Vector3f.div(n: Number): Vector3f = Vector3f(this).div(n.toFloat())

operator fun Vector3f.compareTo(n: Int) = length().compareTo(n)
operator fun Vector3f.compareTo(n: Long) = length().compareTo(n)
operator fun Vector3f.compareTo(n: Float) = length().compareTo(n)
operator fun Vector3f.compareTo(n: Double) = length().compareTo(n)
operator fun Vector3f.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vector3f.plus(vec: Vector3f): Vector3f = Vector3f(this).add(vec)

operator fun Vector3f.minus(vec: Vector3f): Vector3f = Vector3f(this).sub(vec)

operator fun Vector3f.times(vec: Vector3f): Vector3f = Vector3f(this).mul(vec)

operator fun Vector3f.div(vec: Vector3f): Vector3f = Vector3f(this).div(vec)

infix fun Vector3f.cross(vec: Vector3f): Vector3f = Vector3f(this).cross(vec)

infix fun Vector3f.dot(vec: Vector3f): Float = Vector3f(this).dot(vec)

operator fun Vector3f.component1() = x
operator fun Vector3f.component2() = y
operator fun Vector3f.component3() = z


// Vector3d operations

operator fun Vector3d.unaryMinus(): Vector3d = Vector3d(this).mul(-1.0)

operator fun Vector3d.not(): Vector3d = Vector3d(this).mul(-1.0)

operator fun Vector3d.plus(n: Int): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).add(nDouble, nDouble, nDouble) }
operator fun Vector3d.plus(n: Long): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).add(nDouble, nDouble, nDouble) }
operator fun Vector3d.plus(n: Float): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).add(nDouble, nDouble, nDouble) }
operator fun Vector3d.plus(n: Double): Vector3d = Vector3d(this).add(n, n, n)
operator fun Vector3d.plus(n: Number): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).add(nDouble, nDouble, nDouble) }

operator fun Vector3d.minus(n: Int): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).sub(nDouble, nDouble, nDouble) }
operator fun Vector3d.minus(n: Long): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).sub(nDouble, nDouble, nDouble) }
operator fun Vector3d.minus(n: Float): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).sub(nDouble, nDouble, nDouble) }
operator fun Vector3d.minus(n: Double): Vector3d = Vector3d(this).sub(n, n, n)
operator fun Vector3d.minus(n: Number): Vector3d = n.toDouble().let { nDouble -> Vector3d(this).sub(nDouble, nDouble, nDouble) }

operator fun Vector3d.times(n: Int): Vector3d = Vector3d(this).mul(n.toDouble())
operator fun Vector3d.times(n: Long): Vector3d = Vector3d(this).mul(n.toDouble())
operator fun Vector3d.times(n: Float): Vector3d = Vector3d(this).mul(n.toDouble())
operator fun Vector3d.times(n: Double): Vector3d = Vector3d(this).mul(n)
operator fun Vector3d.times(n: Number): Vector3d = Vector3d(this).mul(n.toDouble())

operator fun Vector3d.div(n: Int): Vector3d = Vector3d(this).div(n.toDouble())
operator fun Vector3d.div(n: Long): Vector3d = Vector3d(this).div(n.toDouble())
operator fun Vector3d.div(n: Float): Vector3d = Vector3d(this).div(n.toDouble())
operator fun Vector3d.div(n: Double): Vector3d = Vector3d(this).div(n)
operator fun Vector3d.div(n: Number): Vector3d = Vector3d(this).div(n.toDouble())

operator fun Vector3d.compareTo(n: Int) = length().compareTo(n)
operator fun Vector3d.compareTo(n: Long) = length().compareTo(n)
operator fun Vector3d.compareTo(n: Float) = length().compareTo(n)
operator fun Vector3d.compareTo(n: Double) = length().compareTo(n)
operator fun Vector3d.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vector3d.plus(vec: Vector3d): Vector3d = Vector3d(this).add(vec)

operator fun Vector3d.minus(vec: Vector3d): Vector3d = Vector3d(this).sub(vec)

operator fun Vector3d.times(vec: Vector3d): Vector3d = Vector3d(this).mul(vec)

operator fun Vector3d.div(vec: Vector3d): Vector3d = Vector3d(this).div(vec)

infix fun Vector3d.cross(vec: Vector3d): Vector3d = Vector3d(this).cross(vec)

infix fun Vector3d.dot(vec: Vector3d): Double = Vector3d(this).dot(vec)

operator fun Vector3d.component1() = x
operator fun Vector3d.component2() = y
operator fun Vector3d.component3() = z


// Vec2 operations

operator fun Vec2.unaryMinus(): Vec2 = negated()

operator fun Vec2.not(): Vec2 = negated()

operator fun Vec2.plus(n: Int): Vec2 = add(n.toFloat())
operator fun Vec2.plus(n: Long): Vec2 = add(n.toFloat())
operator fun Vec2.plus(n: Float): Vec2 = add(n)
operator fun Vec2.plus(n: Double): Vec2 = add(n.toFloat())
operator fun Vec2.plus(n: Number): Vec2 = add(n.toFloat())

operator fun Vec2.minus(n: Int): Vec2 = plus(-n)
operator fun Vec2.minus(n: Long): Vec2 = plus(-n)
operator fun Vec2.minus(n: Float): Vec2 = plus(-n)
operator fun Vec2.minus(n: Double): Vec2 = plus(-n)
operator fun Vec2.minus(n: Number): Vec2 = plus(-n.toDouble())

operator fun Vec2.times(n: Int): Vec2 = scale(n.toFloat())
operator fun Vec2.times(n: Long): Vec2 = scale(n.toFloat())
operator fun Vec2.times(n: Float): Vec2 = scale(n)
operator fun Vec2.times(n: Double): Vec2 = scale(n.toFloat())
operator fun Vec2.times(n: Number): Vec2 = scale(n.toFloat())

operator fun Vec2.div(n: Int): Vec2 = scale(1.0f / n.toFloat())
operator fun Vec2.div(n: Long): Vec2 = scale(1.0f / n.toFloat())
operator fun Vec2.div(n: Float): Vec2 = scale(1.0f / n)
operator fun Vec2.div(n: Double): Vec2 = scale(1.0f / n.toFloat())
operator fun Vec2.div(n: Number): Vec2 = scale(1.0f / n.toFloat())

operator fun Vec2.compareTo(n: Int) = length().compareTo(n)
operator fun Vec2.compareTo(n: Long) = length().compareTo(n)
operator fun Vec2.compareTo(n: Float) = length().compareTo(n)
operator fun Vec2.compareTo(n: Double) = length().compareTo(n)
operator fun Vec2.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vec2.plus(vec: Vec2): Vec2 = add(vec)

operator fun Vec2.minus(vec: Vec2): Vec2 = add(-vec)

operator fun Vec2.times(vec: Vec2): Vec2 = Vec2(x * vec.x, y * vec.y)

operator fun Vec2.div(vec: Vec2): Vec2 = Vec2(x / vec.x, y / vec.y)

infix fun Vec2.dot(vec: Vec2): Float = dot(vec)

operator fun Vec2.component1() = x
operator fun Vec2.component2() = y


// Vector2i operations

operator fun Vector2i.unaryMinus(): Vector2i = Vector2i(this).mul(-1)

operator fun Vector2i.not(): Vector2i = Vector2i(this).mul(-1)

operator fun Vector2i.plus(n: Int): Vector2i = Vector2i(this).add(n, n)
operator fun Vector2i.plus(n: Long): Vector2i = n.toInt().let { nInt -> Vector2i(this).add(nInt, nInt) }
operator fun Vector2i.plus(n: Float): Vector2i = n.toInt().let { nInt -> Vector2i(this).add(nInt, nInt) }
operator fun Vector2i.plus(n: Double): Vector2i = n.toInt().let { nInt -> Vector2i(this).add(nInt, nInt) }
operator fun Vector2i.plus(n: Number): Vector2i = n.toInt().let { nInt -> Vector2i(this).add(nInt, nInt) }

operator fun Vector2i.minus(n: Int): Vector2i = Vector2i(this).sub(n, n)
operator fun Vector2i.minus(n: Long): Vector2i = n.toInt().let { nInt -> Vector2i(this).sub(nInt, nInt) }
operator fun Vector2i.minus(n: Float): Vector2i = n.toInt().let { nInt -> Vector2i(this).sub(nInt, nInt) }
operator fun Vector2i.minus(n: Double): Vector2i = n.toInt().let { nInt -> Vector2i(this).sub(nInt, nInt) }
operator fun Vector2i.minus(n: Number): Vector2i = n.toInt().let { nInt -> Vector2i(this).sub(nInt, nInt) }

operator fun Vector2i.times(n: Int): Vector2i = Vector2i(this).mul(n)
operator fun Vector2i.times(n: Long): Vector2i = Vector2i(this).mul(n.toInt())
operator fun Vector2i.times(n: Float): Vector2i = n.toDouble().let { nDouble -> Vector2i((x * nDouble).toInt(), (y * nDouble).toInt()) }
operator fun Vector2i.times(n: Double): Vector2i = Vector2i((x * n).toInt(), (y * n).toInt())
operator fun Vector2i.times(n: Number): Vector2i = n.toDouble().let { nDouble -> Vector2i((x * nDouble).toInt(), (y * nDouble).toInt()) }

operator fun Vector2i.div(n: Int): Vector2i = Vector2i(this).div(n)
operator fun Vector2i.div(n: Long): Vector2i = Vector2i(this).div(n.toInt())
operator fun Vector2i.div(n: Float): Vector2i = Vector2i(this).div(n)
operator fun Vector2i.div(n: Double): Vector2i = Vector2i(this).div(n.toFloat())
operator fun Vector2i.div(n: Number): Vector2i = Vector2i(this).div(n.toFloat())

operator fun Vector2i.compareTo(n: Int) = length().compareTo(n)
operator fun Vector2i.compareTo(n: Long) = length().compareTo(n)
operator fun Vector2i.compareTo(n: Float) = length().compareTo(n)
operator fun Vector2i.compareTo(n: Double) = length().compareTo(n)
operator fun Vector2i.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vector2i.plus(vec: Vector2i): Vector2i = Vector2i(this).add(vec)

operator fun Vector2i.minus(vec: Vector2i): Vector2i = Vector2i(this).sub(vec)

operator fun Vector2i.times(vec: Vector2i): Vector2i = Vector2i(this).mul(vec)

operator fun Vector2i.div(vec: Vector2i): Vector2i = Vector2i(x / vec.x, y / vec.y)

infix fun Vector2i.dot(vec: Vector2i): Double = ((x * vec.x) + (y * vec.y)).toDouble()

operator fun Vector2i.component1() = x
operator fun Vector2i.component2() = y


// Vector2f operations

operator fun Vector2f.unaryMinus(): Vector2f = Vector2f(this).mul(-1.0f)

operator fun Vector2f.not(): Vector2f = Vector2f(this).mul(-1.0f)

operator fun Vector2f.plus(n: Int): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).add(nFloat, nFloat) }
operator fun Vector2f.plus(n: Long): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).add(nFloat, nFloat) }
operator fun Vector2f.plus(n: Float): Vector2f = Vector2f(this).add(n, n)
operator fun Vector2f.plus(n: Double): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).add(nFloat, nFloat) }
operator fun Vector2f.plus(n: Number): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).add(nFloat, nFloat) }

operator fun Vector2f.minus(n: Int): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).sub(nFloat, nFloat) }
operator fun Vector2f.minus(n: Long): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).sub(nFloat, nFloat) }
operator fun Vector2f.minus(n: Float): Vector2f = Vector2f(this).sub(n, n)
operator fun Vector2f.minus(n: Double): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).sub(nFloat, nFloat) }
operator fun Vector2f.minus(n: Number): Vector2f = n.toFloat().let { nFloat -> Vector2f(this).sub(nFloat, nFloat) }

operator fun Vector2f.times(n: Int): Vector2f = Vector2f(this).mul(n.toFloat())
operator fun Vector2f.times(n: Long): Vector2f = Vector2f(this).mul(n.toFloat())
operator fun Vector2f.times(n: Float): Vector2f = Vector2f(this).mul(n)
operator fun Vector2f.times(n: Double): Vector2f = Vector2f(this).mul(n.toFloat())
operator fun Vector2f.times(n: Number): Vector2f = Vector2f(this).mul(n.toFloat())

operator fun Vector2f.div(n: Int): Vector2f = Vector2f(this).div(n.toFloat())
operator fun Vector2f.div(n: Long): Vector2f = Vector2f(this).div(n.toFloat())
operator fun Vector2f.div(n: Float): Vector2f = Vector2f(this).div(n)
operator fun Vector2f.div(n: Double): Vector2f = Vector2f(this).div(n.toFloat())
operator fun Vector2f.div(n: Number): Vector2f = Vector2f(this).div(n.toFloat())

operator fun Vector2f.compareTo(n: Int) = length().compareTo(n)
operator fun Vector2f.compareTo(n: Long) = length().compareTo(n)
operator fun Vector2f.compareTo(n: Float) = length().compareTo(n)
operator fun Vector2f.compareTo(n: Double) = length().compareTo(n)
operator fun Vector2f.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vector2f.plus(vec: Vector2f): Vector2f = Vector2f(this).add(vec)

operator fun Vector2f.minus(vec: Vector2f): Vector2f = Vector2f(this).sub(vec)

operator fun Vector2f.times(vec: Vector2f): Vector2f = Vector2f(this).mul(vec)

operator fun Vector2f.div(vec: Vector2f): Vector2f = Vector2f(this).div(vec)

infix fun Vector2f.dot(vec: Vector2f): Float = Vector2f(this).dot(vec)

operator fun Vector2f.component1() = x
operator fun Vector2f.component2() = y


// Vector2d operations

operator fun Vector2d.unaryMinus(): Vector2d = Vector2d(this).mul(-1.0)

operator fun Vector2d.not(): Vector2d = Vector2d(this).mul(-1.0)

operator fun Vector2d.plus(n: Int): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).add(nDouble, nDouble) }
operator fun Vector2d.plus(n: Long): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).add(nDouble, nDouble) }
operator fun Vector2d.plus(n: Float): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).add(nDouble, nDouble) }
operator fun Vector2d.plus(n: Double): Vector2d = Vector2d(this).add(n, n)
operator fun Vector2d.plus(n: Number): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).add(nDouble, nDouble) }

operator fun Vector2d.minus(n: Int): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).sub(nDouble, nDouble) }
operator fun Vector2d.minus(n: Long): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).sub(nDouble, nDouble) }
operator fun Vector2d.minus(n: Float): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).sub(nDouble, nDouble) }
operator fun Vector2d.minus(n: Double): Vector2d = Vector2d(this).sub(n, n)
operator fun Vector2d.minus(n: Number): Vector2d = n.toDouble().let { nDouble -> Vector2d(this).sub(nDouble, nDouble) }

operator fun Vector2d.times(n: Int): Vector2d = Vector2d(this).mul(n.toDouble())
operator fun Vector2d.times(n: Long): Vector2d = Vector2d(this).mul(n.toDouble())
operator fun Vector2d.times(n: Float): Vector2d = Vector2d(this).mul(n.toDouble())
operator fun Vector2d.times(n: Double): Vector2d = Vector2d(this).mul(n)
operator fun Vector2d.times(n: Number): Vector2d = Vector2d(this).mul(n.toDouble())

operator fun Vector2d.div(n: Int): Vector2d = Vector2d(this).div(n.toDouble())
operator fun Vector2d.div(n: Long): Vector2d = Vector2d(this).div(n.toDouble())
operator fun Vector2d.div(n: Float): Vector2d = Vector2d(this).div(n.toDouble())
operator fun Vector2d.div(n: Double): Vector2d = Vector2d(this).div(n)
operator fun Vector2d.div(n: Number): Vector2d = Vector2d(this).div(n.toDouble())

operator fun Vector2d.compareTo(n: Int) = length().compareTo(n)
operator fun Vector2d.compareTo(n: Long) = length().compareTo(n)
operator fun Vector2d.compareTo(n: Float) = length().compareTo(n)
operator fun Vector2d.compareTo(n: Double) = length().compareTo(n)
operator fun Vector2d.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vector2d.plus(vec: Vector2d): Vector2d = Vector2d(this).add(vec)

operator fun Vector2d.minus(vec: Vector2d): Vector2d = Vector2d(this).sub(vec)

operator fun Vector2d.times(vec: Vector2d): Vector2d = Vector2d(this).mul(vec)

operator fun Vector2d.div(vec: Vector2d): Vector2d = Vector2d(this).div(vec)

infix fun Vector2d.dot(vec: Vector2d): Double = Vector2d(this).dot(vec)

operator fun Vector2d.component1() = x
operator fun Vector2d.component2() = y
