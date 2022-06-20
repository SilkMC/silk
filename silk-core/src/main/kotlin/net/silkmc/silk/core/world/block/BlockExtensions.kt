package net.silkmc.silk.core.world.block

import net.silkmc.silk.core.mixin.block.AbstractBlockAccessor
import net.minecraft.world.level.block.Block

/**
 * Returns true, if the block settings of this block say that this block
 * has a collision box.
 */
val Block.isCollidable get() = (this as AbstractBlockAccessor).isCollidable
