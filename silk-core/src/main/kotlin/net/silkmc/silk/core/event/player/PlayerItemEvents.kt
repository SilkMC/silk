package net.silkmc.silk.core.event.player

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.silkmc.silk.core.event.Event
import net.silkmc.silk.core.event.EventScope

/**
 * Events related to Items and [net.minecraft.world.entity.player.Player]s
 */
val PlayerEvents.Item get() = PlayerItemEvents

object PlayerItemEvents {

    class PlayerItemEvent(val item: ItemStack, level: Level, player: Player) : PlayerEvents.PlayerEvent(player)

    /**
     * Called before an item gets collected
     */
    val itemCollect = Event.syncAsync<PlayerItemEvent, EventScope.Cancellable> { EventScope.Cancellable() }



}