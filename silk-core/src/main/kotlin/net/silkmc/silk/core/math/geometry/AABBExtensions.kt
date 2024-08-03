@file:Suppress("unused", "EXTENSION_SHADOWED_BY_MEMBER")

package net.silkmc.silk.core.math.geometry

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.silkmc.silk.core.math.vector.unaryMinus
import org.joml.Vector3f

fun AABB.inflate(value: Int): AABB = inflate(value.toDouble())
fun AABB.expand(value: Int): AABB = inflate(value) // for yarn mappings (inflate is expand in yarn)
fun AABB.inflate(value: Number): AABB = inflate(value.toDouble())
fun AABB.expand(value: Number): AABB = inflate(value) // for yarn mappings (inflate is expand in yarn)

fun AABB.deflate(value: Int): AABB = deflate(value.toDouble())
fun AABB.contract(value: Int): AABB = deflate(value) // for yarn mappings (deflate is contract in yarn)
fun AABB.deflate(value: Number): AABB = deflate(value.toDouble())
fun AABB.contract(value: Number): AABB = deflate(value) // for yarn mappings (deflate is contract in yarn)

fun AABB.expandTowards(x: Int, y: Int, z: Int): AABB = expandTowards(x.toDouble(), y.toDouble(), z.toDouble())
fun AABB.stretch(x: Int, y: Int, z: Int): AABB = expandTowards(x, y, z) // for yarn mappings (expandTowards is stretch in yarn)
fun AABB.expandTowards(x: Number, y: Number, z: Number): AABB = expandTowards(x.toDouble(), y.toDouble(), z.toDouble())
fun AABB.stretch(x: Number, y: Number, z: Number): AABB = expandTowards(x, y, z) // for yarn mappings (expandTowards is stretch in yarn)

infix fun AABB.minmax(aabb: AABB): AABB = minmax(aabb)
infix fun AABB.union(aabb: AABB): AABB = minmax(aabb) // for yarn mappings (minmax is union in yarn)

operator fun AABB.contains(aabb: AABB) = intersects(aabb)
operator fun AABB.contains(pos: Vec3i) = contains(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
operator fun AABB.contains(pos: Vec3) = contains(pos.x, pos.y, pos.z)

operator fun AABB.plus(pos: BlockPos): AABB = move(pos)
operator fun AABB.plus(pos: Vec3): AABB = move(pos)
operator fun AABB.plus(pos: Vector3f): AABB = move(pos)

operator fun AABB.minus(pos: BlockPos): AABB = move(-pos)
operator fun AABB.minus(pos: Vec3): AABB = move(-pos)
operator fun AABB.minus(pos: Vector3f): AABB = move(-pos)
