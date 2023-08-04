package net.silkmc.silk.core.event

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

/**
 * Events related to an [net.minecraft.world.level.block.Block].
 */
@Suppress("unused") // receiver is for namespacing only
val Events.Block get() = BlockEvents

@ExperimentalSilkApi
object BlockEvents {

    class BlockEvent(val level: Level, val pos: BlockPos, val blockState: BlockState)

    /**
     * Called before a block gets set
     */
    val prePlace = Event.syncAsync<BlockEvent, EventScope.Cancellable> {
        EventScope.Cancellable()
    }

    /**
     * Called before a block gets destroyed
     */
    val preDestroy = Event.syncAsync<BlockEvent, EventScope.Cancellable> {
        EventScope.Cancellable()
    }
}
