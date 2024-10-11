package net.silkmc.silk.core.entity

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Relative
import net.minecraft.world.level.portal.TeleportTransition
import net.minecraft.world.phys.Vec3

/**
 * Correctly handles teleports for all kinds of entities. Differentiates between [ServerPlayer],
 * [LivingEntity] and [Entity] and calls the correct function for each of them. Handles [ServerLevel]
 * and direction changes.
 */
fun Entity.changePos(
    x: Number = this.position().x,
    y: Number = this.position().y,
    z: Number = this.position().z,
    world: ServerLevel? = null,
    yaw: Float? = null,
    pitch: Float? = null,
) {
    val xD = x.toDouble()
    val yD = y.toDouble()
    val zD = z.toDouble()

    if (world != null && this is ServerPlayer) {
        teleportTo(world, xD, yD, zD, Relative.ALL, yaw ?: this.yRot, pitch ?: this.xRot, true)
        return
    }

    if (world != null && world != this.level()) {
        teleport(TeleportTransition(
            world, Vec3(xD, yD, zD), Vec3.ZERO, yaw ?: this.yRot,
            pitch ?: this.xRot, Relative.ALL, TeleportTransition.DO_NOTHING
        ))
        return
    }

    if (yaw != null) this.yRot = yaw
    if (pitch != null) this.xRot = pitch

    teleportTo(xD, yD, zD)
}

/**
 * Schedules all necessary updates. Packets will be sent to the players to inform
 * them about the new velocity.
 */
fun Entity.markVelocityDirty() {
    hasImpulse = true
    hurtMarked = true
}

/**
 * Changes the velocity of this [Entity] and calls [markVelocityDirty].
 *
 * @param add Whether the velocity should be added to the current one or not. Set this to false
 * if you want to overwrite the previous velocity.
 */
fun Entity.modifyVelocity(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0, add: Boolean = true) {
    deltaMovement = if (add) {
        deltaMovement.add(x.toDouble(), y.toDouble(), z.toDouble())
    } else {
        Vec3(x.toDouble(), y.toDouble(), z.toDouble())
    }

    markVelocityDirty()
}

/**
 * Changes the velocity of this [Entity] using the given mutation logic in [block].
 * After that, [markVelocityDirty] is called.
 */
inline fun Entity.modifyVelocity(block: (Vec3) -> Vec3) {
    deltaMovement = block(deltaMovement)
    markVelocityDirty()
}

/**
 * Sets the [Entity]s velocity to [vec] and calls [markVelocityDirty].
 */
fun Entity.modifyVelocity(vec: Vec3) {
    deltaMovement = vec
    markVelocityDirty()
}
