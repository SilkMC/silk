package net.axay.fabrik.core.entity

import net.axay.fabrik.core.world.block.BlockInfo
import net.axay.fabrik.core.world.block.isCollidable
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3

val Entity.pos: Vec3 get() = position()

/**
 * Returns the pos under the entities "feet".
 */
val Entity.posUnder: BlockPos
    get() = pos.run { BlockPos(x, y - 0.05, z) }

/**
 * Returns an instance of [BlockInfo] of the block the entity is currently standing on
 * or swimming in, else it will be the [BlockInfo] of air - use [touchedBlockNoAir] if
 * you don't need any info about air.
 */
val Entity.touchedBlock: BlockInfo
    get() {
        if (isOnGround) {
            val posBelow = posUnder

            val stateBelow = level.getBlockState(posBelow)
            if (stateBelow.block.isCollidable) {
                return BlockInfo(stateBelow, posBelow)
            } else {
                val posDown = posBelow.below()
                val stateDown = level.getBlockState(posDown)
                val collisionShape = stateDown.getCollisionShape(level, posDown)
                if (!collisionShape.isEmpty && collisionShape.bounds().ysize > 1) {
                    return BlockInfo(stateDown, posDown)
                }

                return onPos.let { BlockInfo(level.getBlockState(it), it) }
            }
        } else {
            val posHere = blockPosition()

            if (isInWater || isInLava) {
                val stateHere = level.getBlockState(posHere)
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
