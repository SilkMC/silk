package net.axay.fabrik.core.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

/**
 * Correctly handles teleports for all kinds of entities. Differentiates between [ServerPlayerEntity],
 * [LivingEntity] and [Entity] and calls the correct function for each of them. Handles [ServerWorld]
 * and direction changes.
 */
fun Entity.changePos(
    x: Number = this.pos.x,
    y: Number = this.pos.y,
    z: Number = this.pos.z,
    world: ServerWorld? = null,
    yaw: Float? = null,
    pitch: Float? = null,
) {
    val xD = x.toDouble()
    val yD = y.toDouble()
    val zD = z.toDouble()

    if (world != null && this is ServerPlayerEntity) {
        teleport(world, xD, yD, zD, yaw ?: this.yaw, pitch ?: this.pitch)
        return
    }

    if (world != null && world != this.world) {
        moveToWorld(world)
    }

    if (yaw != null) this.yaw = yaw
    if (pitch != null) this.pitch = pitch

    if (this is LivingEntity) {
        teleport(xD, yD, zD, false)
    } else {
        teleport(xD, yD, zD)
    }
}

/**
 * Schedules all necessary updates. Packets will be sent to the players to inform
 * them about the new velocity.
 */
fun Entity.markVelocityDirty() {
    velocityDirty = true
    velocityModified = true
}

/**
 * Changes the velocity of this [Entity] and calls [markVelocityDirty].
 *
 * @param add Whether the velocity should be added to the current one or not. Set this to false
 * if you want to overwrite the previous velocity.
 */
fun Entity.modifyVelocity(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0, add: Boolean = true) {
    velocity = if (add) {
        velocity.add(x.toDouble(), y.toDouble(), z.toDouble())
    } else {
        Vec3d(x.toDouble(), y.toDouble(), z.toDouble())
    }

    markVelocityDirty()
}

/**
 * Changes the velocity of this [Entity] using the given mutation logic in [block].
 * After that, [markVelocityDirty] is called.
 */
inline fun Entity.modifyVelocity(block: (Vec3d) -> Vec3d) {
    velocity = block(velocity)
    markVelocityDirty()
}
