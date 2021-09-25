package net.axay.fabrik.core.entity

import net.axay.fabrik.core.world.block.BlockInfo
import net.axay.fabrik.core.world.block.isCollidable
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos

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

            val stateBelow = world.getBlockState(posBelow)
            if (stateBelow.block.isCollidable) {
                return BlockInfo(stateBelow, posBelow)
            } else {
                val posDown = posBelow.down()
                val stateDown = world.getBlockState(posDown)
                val collisionShape = stateDown.getCollisionShape(world, posDown)
                if (!collisionShape.isEmpty && collisionShape.boundingBox.yLength > 1) {
                    return BlockInfo(stateDown, posDown)
                }

                return landingPos.let { BlockInfo(world.getBlockState(it), it) }
            }
        } else {
            val posHere = blockPos

            if (isTouchingWater || isInLava) {
                val stateHere = world.getBlockState(posHere)
                if (!stateHere.fluidState.isEmpty) {
                    return BlockInfo(stateHere, posHere)
                }
            }

            return BlockInfo(Blocks.AIR.defaultState, posHere)
        }
    }

/**
 * Does the same as [touchedBlock], but returns null if the block is air.
 */
val Entity.touchedBlockNoAir
    get() = touchedBlock.let { if (it.state.isAir) null else it }
