@file:Suppress("unused")

package net.silkmc.silk.core.world

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.level.CollisionGetter
import net.minecraft.world.level.EntityGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.shapes.VoxelShape
import net.silkmc.silk.core.kotlin.asKotlinRandom
import kotlin.random.Random

val Level.kotlinRandom: Random
    get() = random.asKotlinRandom()

fun CollisionGetter.entityCollisions(
    entity: Entity? = null,
    box: AABB,
): List<VoxelShape> {
    return getEntityCollisions(entity, box)
}

fun CollisionGetter.collisions(
    entity: Entity? = null,
    box: AABB,
): Iterable<VoxelShape> {
    return getCollisions(entity, box)
}

fun CollisionGetter.blockCollisions(
    entity: Entity? = null,
    box: AABB,
): Iterable<VoxelShape> {
    return getBlockCollisions(entity, box)
}

fun EntityGetter.entityCollisions(
    entity: Entity? = null,
    box: AABB,
): List<VoxelShape> {
    return getEntityCollisions(entity, box)
}

fun EntityGetter.entities(
    except: Entity? = null,
    box: AABB,
    predicate: (Entity) -> Boolean = EntitySelector.NO_SPECTATORS::test,
): List<Entity> {
    return getEntities(except, box, predicate)
}

inline fun <reified T : Entity> EntityGetter.entitiesByType(
    box: AABB,
    noinline predicate: (T) -> Boolean = EntitySelector.NO_SPECTATORS::test,
): List<T> {
    return getEntities(EntityTypeTest.forClass(T::class.java), box, predicate)
}

inline fun <reified T : Entity> EntityGetter.entitiesByClass(
    box: AABB,
    noinline predicate: (T) -> Boolean = EntitySelector.NO_SPECTATORS::test,
): List<T> {
    return getEntitiesOfClass(T::class.java, box, predicate)
}
