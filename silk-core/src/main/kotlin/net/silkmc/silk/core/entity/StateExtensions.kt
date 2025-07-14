package net.silkmc.silk.core.entity

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import net.silkmc.silk.core.world.block.BlockInfo
import net.silkmc.silk.core.world.block.isCollidable
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

/**
 * Shortcut property accessor for [Entity.position].
 */
val Entity.pos: Vec3 get() = position()

/**
 * Shortcut property accessor for [Entity.blockPosition].
 */
val Entity.blockPos: BlockPos get() = blockPosition()

/**
 * Shortcut property accessor for [Entity.level].
 */
val Entity.world: Level get() = level()

/**
 * Shortcut property accessor for [ServerPlayer.level].
 */
val ServerPlayer.serverWorld: ServerLevel get() = level()

/**
 * Returns the pos under the entities "feet".
 */
val Entity.posUnder: BlockPos
    get() {
        val exactPos = pos
        val posForXZ = blockPos
        return BlockPos(posForXZ.x, floor(exactPos.y - 0.05).toInt(), posForXZ.z)
    }

/**
 * Returns a unit-vector pointing in the direction the entity
 * is looking.
 */
val Entity.directionVector: Vec3
    get() {
        val rotY = Math.toRadians(yRot.toDouble())
        val rotX = Math.toRadians(xRot.toDouble())
        val xz = cos(rotX)
        return Vec3(-xz * sin(rotY), -sin(rotX), xz * cos(rotY))
    }

/**
 * Returns an instance of [BlockInfo] of the block the entity is currently standing on
 * or swimming in, else it will be the [BlockInfo] of air - use [touchedBlockNoAir] if
 * you don't need any info about air.
 */
val Entity.touchedBlock: BlockInfo
    get() {
        if (onGround()) {
            val posBelow = posUnder

            val stateBelow = world.getBlockState(posBelow)
            if (stateBelow.block.isCollidable) {
                return BlockInfo(stateBelow, posBelow)
            } else {
                val posDown = posBelow.below()
                val stateDown = world.getBlockState(posDown)
                val collisionShape = stateDown.getCollisionShape(world, posDown)
                if (!collisionShape.isEmpty && collisionShape.bounds().ysize > 1) {
                    return BlockInfo(stateDown, posDown)
                }

                return onPos.let { BlockInfo(world.getBlockState(it), it) }
            }
        } else {
            val posHere = blockPosition()

            if (isInWater || isInLava) {
                val stateHere = world.getBlockState(posHere)
                if (!stateHere.fluidState.isEmpty) {
                    return BlockInfo(stateHere, posHere)
                }
            }

            return BlockInfo(Blocks.AIR.defaultBlockState(), posHere)
        }
    }

/**
 * Does the same as [touchedBlock], but returns null if the block is air.
 */
val Entity.touchedBlockNoAir
    get() = touchedBlock.let { if (it.state.isAir) null else it }
