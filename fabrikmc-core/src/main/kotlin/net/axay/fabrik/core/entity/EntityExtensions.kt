package net.axay.fabrik.core.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

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
