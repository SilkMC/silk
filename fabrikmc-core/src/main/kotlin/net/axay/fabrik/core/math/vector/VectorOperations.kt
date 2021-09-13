package net.axay.fabrik.core.math.vector

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import net.minecraft.util.math.Vec3i

operator fun Vec3d.not(): Vec3d = negate()

operator fun Vec3d.plus(n: Number): Vec3d = n.toDouble().let { nDouble -> add(nDouble, nDouble, nDouble) }
operator fun Vec3d.minus(n: Number): Vec3d = n.toDouble().let { nDouble -> subtract(nDouble, nDouble, nDouble) }
operator fun Vec3d.times(n: Number): Vec3d = multiply(n.toDouble())
operator fun Vec3d.div(n: Number): Vec3d = multiply(1.0 / n.toDouble())
operator fun Vec3d.compareTo(n: Number) = length().compareTo(n.toDouble())

operator fun Vec3d.plus(vec: Vec3d): Vec3d = add(vec)
operator fun Vec3d.minus(vec: Vec3d): Vec3d = subtract(vec)
operator fun Vec3d.times(vec: Vec3d): Vec3d = multiply(vec)
operator fun Vec3d.div(vec: Vec3d): Vec3d = Vec3d(x / vec.x, y / vec.y, z / vec.z)
operator fun Vec3d.compareTo(vec: Vec3d) = lengthSquared().compareTo(vec.lengthSquared())


operator fun Vec3f.not(): Vec3f = times(-1f)

operator fun Vec3f.plus(n: Number): Vec3f = copy().apply { n.toFloat().let { nFloat -> add(nFloat, nFloat, nFloat) } }
operator fun Vec3f.minus(n: Number): Vec3f = copy().apply { n.toFloat().let { nFloat -> subtract(Vec3f(nFloat, nFloat, nFloat)) } }
operator fun Vec3f.times(n: Number): Vec3f = n.toFloat().let { nFloat -> Vec3f(x * nFloat, y * nFloat, z * nFloat) }
operator fun Vec3f.div(n: Number): Vec3f = times(1.0 / n.toDouble())
operator fun Vec3f.compareTo(n: Number) = Vec3d(this).length().compareTo(n.toDouble())

operator fun Vec3f.plus(vec: Vec3f): Vec3f = copy().apply { add(vec) }
operator fun Vec3f.minus(vec: Vec3f): Vec3f = copy().apply { subtract(vec) }
operator fun Vec3f.times(vec: Vec3f): Vec3f = Vec3f(x * vec.x, y * vec.y, z * vec.z)
operator fun Vec3f.div(vec: Vec3f): Vec3f = Vec3f(x / vec.x, y / vec.y, z / vec.z)
operator fun Vec3f.compareTo(vec: Vec3f) = Vec3d(this).lengthSquared().compareTo(Vec3d(vec).lengthSquared())


operator fun Vec3i.not(): Vec3i = times(-1)

inline operator fun <reified T : Vec3i> T.plus(n: Number): T = n.toInt().let { nInt -> add(nInt, nInt, nInt) } as T
inline operator fun <reified T : Vec3i> T.minus(n: Number): T = (-n.toInt()).let { nInt -> add(nInt, nInt, nInt) } as T
operator fun Vec3i.times(n: Number): Vec3i = multiply(n.toInt())
operator fun Vec3i.div(n: Number) = (1.0 / n.toDouble()).let { nDouble -> Vec3i(x * nDouble, y * nDouble, z * nDouble) }
operator fun Vec3i.compareTo(n: Number) = Vec3d(x.toDouble(), y.toDouble(), z.toDouble()).length().compareTo(n.toDouble())

inline operator fun <reified T : Vec3i> T.plus(vec: Vec3i): T = add(vec) as T
inline operator fun <reified T : Vec3i> T.minus(vec: Vec3i): T = add(!vec) as T
operator fun Vec3i.times(vec: Vec3i): Vec3i = Vec3i(x * vec.x, y * vec.y, z * vec.z)
operator fun Vec3i.div(vec: Vec3i): Vec3i = Vec3i(x / vec.x, y / vec.y, z / vec.z)
operator fun Vec3i.compareTo(vec: Vec3i) = Vec3d(x.toDouble(), y.toDouble(), z.toDouble()).lengthSquared().compareTo(Vec3d(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble()).lengthSquared())
