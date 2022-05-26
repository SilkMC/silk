package net.axay.fabrik.core.math.vector

import com.mojang.math.Vector3f
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3

operator fun Vec3.not(): Vec3 = reverse()

operator fun Vec3.plus(n: Number): Vec3 = n.toDouble().let { nDouble -> add(nDouble, nDouble, nDouble) }
operator fun Vec3.minus(n: Number): Vec3 = n.toDouble().let { nDouble -> subtract(nDouble, nDouble, nDouble) }
operator fun Vec3.times(n: Number): Vec3 = scale(n.toDouble())
operator fun Vec3.div(n: Number): Vec3 = scale(1.0 / n.toDouble())
operator fun Vec3.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vec3.plus(vec: Vec3): Vec3 = add(vec)
operator fun Vec3.minus(vec: Vec3): Vec3 = subtract(vec)
operator fun Vec3.times(vec: Vec3): Vec3 = multiply(vec)
operator fun Vec3.div(vec: Vec3): Vec3 = Vec3(x / vec.x, y / vec.y, z / vec.z)
operator fun Vec3.compareTo(vec: Vec3) = lengthSqr().compareTo(vec.lengthSqr())

operator fun Vec3.component1() = x
operator fun Vec3.component2() = y
operator fun Vec3.component3() = z


operator fun Vector3f.not(): Vector3f = times(-1f)

operator fun Vector3f.plus(n: Number): Vector3f = copy().apply { n.toFloat().let { nFloat -> add(nFloat, nFloat, nFloat) } }
operator fun Vector3f.minus(n: Number): Vector3f = copy().apply { n.toFloat().let { nFloat -> sub(Vector3f(nFloat, nFloat, nFloat)) } }
operator fun Vector3f.times(n: Number): Vector3f = n.toFloat().let { nFloat -> Vector3f(x() * nFloat, y() * nFloat, z() * nFloat) }
operator fun Vector3f.div(n: Number): Vector3f = times(1.0 / n.toDouble())
operator fun Vector3f.compareTo(n: Number) = Vec3(this).length().compareTo(n.toDouble())

operator fun Vector3f.plus(vec: Vector3f): Vector3f = copy().apply { add(vec) }
operator fun Vector3f.minus(vec: Vector3f): Vector3f = copy().apply { sub(vec) }
operator fun Vector3f.times(vec: Vector3f): Vector3f = Vector3f(x() * vec.x(), y() * vec.y(), z() * vec.z())
operator fun Vector3f.div(vec: Vector3f): Vector3f = Vector3f(x() / vec.x(), y() / vec.y(), z() / vec.z())
operator fun Vector3f.compareTo(vec: Vector3f) = Vec3(this).lengthSqr().compareTo(Vec3(vec).lengthSqr())

operator fun Vector3f.component1() = x()
operator fun Vector3f.component2() = y()
operator fun Vector3f.component3() = z()


operator fun Vec3i.not(): Vec3i = times(-1)

inline operator fun <reified T : Vec3i> T.plus(n: Number): T = n.toInt().let { nInt -> offset(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.minus(n: Number): T = (-n.toInt()).let { nInt -> offset(nInt, nInt, nInt) } as T
operator fun Vec3i.times(n: Number): Vec3i = multiply(n.toInt())
operator fun Vec3i.div(n: Number) = (1.0 / n.toDouble()).let { nDouble -> Vec3i(x * nDouble, y * nDouble, z * nDouble) }
operator fun Vec3i.compareTo(n: Number) = Vec3(x.toDouble(), y.toDouble(), z.toDouble()).length().compareTo(n.toDouble())

inline operator fun <reified T : Vec3i> T.plus(vec: Vec3i): T = offset(vec) as T
inline operator fun <reified T : Vec3i> T.minus(vec: Vec3i): T = offset(!vec) as T
operator fun Vec3i.times(vec: Vec3i): Vec3i = Vec3i(x * vec.x, y * vec.y, z * vec.z)
operator fun Vec3i.div(vec: Vec3i): Vec3i = Vec3i(x / vec.x, y / vec.y, z / vec.z)
operator fun Vec3i.compareTo(vec: Vec3i) = Vec3(x.toDouble(), y.toDouble(), z.toDouble()).lengthSqr().compareTo(Vec3(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble()).lengthSqr())

operator fun Vec3i.component1() = x
operator fun Vec3i.component2() = y
operator fun Vec3i.component3() = z
