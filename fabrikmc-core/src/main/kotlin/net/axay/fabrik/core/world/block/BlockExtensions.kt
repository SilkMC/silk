package net.axay.fabrik.core.world.block

import net.axay.fabrik.core.mixin.block.AbstractBlockAccessor
import net.minecraft.block.Block

/**
 * Returns true, if the block settings of this block say that this block
 * has a collision box.
 */
val Block.isCollidable get() = (this as AbstractBlockAccessor).isCollidable
