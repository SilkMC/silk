package net.axay.fabrik.core.math.vector

import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

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
