package net.axay.silk.core.world.block

import net.axay.silk.core.mixin.block.AbstractBlockAccessor
import net.minecraft.world.level.block.Block

/**
 * Returns true, if the block settings of this block say that this block
 * has a collision box.
 */
val Block.isCollidable get() = (this as AbstractBlockAccessor).isCollidable
