package net.silkmc.silk.core.math.vector

import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3

operator fun Vec3.not(): Vec3 =
    reverse()

operator fun Vec3.plus(n: Number): Vec3 =
    n.toDouble().let { nDouble -> add(nDouble, nDouble, nDouble) }

operator fun Vec3.minus(n: Number): Vec3 =
    n.toDouble().let { nDouble -> subtract(nDouble, nDouble, nDouble) }

operator fun Vec3.times(n: Number): Vec3 =
    scale(n.toDouble())

operator fun Vec3.div(n: Number): Vec3 =
    scale(1.0 / n.toDouble())

operator fun Vec3.compareTo(n: Number) =
    length().compareTo(n.toDouble())

operator fun Vec3.plus(vec: Vec3): Vec3 =
    add(vec)

operator fun Vec3.minus(vec: Vec3): Vec3 =
    subtract(vec)

operator fun Vec3.times(vec: Vec3): Vec3 =
    multiply(vec)

operator fun Vec3.div(vec: Vec3): Vec3 =
    Vec3(x / vec.x, y / vec.y, z / vec.z)

@Suppress("DeprecatedCallableAddReplaceWith") // a "replace with" quick-fix could be confusing here
@Deprecated(message = "This comparison is unintuitive and should be avoided")
operator fun Vec3.compareTo(vec: Vec3) =
    lengthSqr().compareTo(vec.lengthSqr())

operator fun Vec3.component1() = x
operator fun Vec3.component2() = y
operator fun Vec3.component3() = z


operator fun Vec3i.not(): Vec3i =
    times(-1)

inline operator fun <reified T : Vec3i> T.plus(n: Number): T =
    n.toInt().let { nInt -> offset(nInt, nInt, nInt) } as T

inline operator fun <reified T : Vec3i> T.minus(n: Number): T =
    (-n.toInt()).let { nInt -> offset(nInt, nInt, nInt) } as T

operator fun Vec3i.times(n: Number): Vec3i =
    multiply(n.toInt())

operator fun Vec3i.div(n: Number) =
    (1.0 / n.toDouble()).let { nDouble -> Vec3i((x * nDouble).toInt(), (y * nDouble).toInt(), (z * nDouble).toInt()) }

operator fun Vec3i.compareTo(n: Number) =
    Vec3(x.toDouble(), y.toDouble(), z.toDouble()).length().compareTo(n.toDouble())

inline operator fun <reified T : Vec3i> T.plus(vec: Vec3i): T =
    offset(vec) as T

inline operator fun <reified T : Vec3i> T.minus(vec: Vec3i): T =
    offset(!vec) as T

operator fun Vec3i.times(vec: Vec3i): Vec3i =
    Vec3i(x * vec.x, y * vec.y, z * vec.z)

operator fun Vec3i.div(vec: Vec3i): Vec3i =
    Vec3i(x / vec.x, y / vec.y, z / vec.z)

// there is an internal compareTo function
// operator fun Vec3i.compareTo(vec: Vec3i) = ...

operator fun Vec3i.component1() = x
operator fun Vec3i.component2() = y
operator fun Vec3i.component3() = z
