package net.silkmc.silk.core.event.player

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.silkmc.silk.core.event.Event
import net.silkmc.silk.core.event.EventScope
import net.silkmc.silk.core.event.Events

@Suppress("UnusedReceiverParameter") // receiver is for namespacing only
val Events.Item get() = PlayerItemEvents

object PlayerItemEvents {

    class ServerPlayerItemEvent(val item: ItemStack, player: ServerPlayer) : PlayerEvents.ServerPlayerEvent(player)

    /**
     * Called before an item gets collected by a player
     */
    val preItemCollect = Event.syncAsync<ServerPlayerItemEvent, EventScope.Cancellable> { EventScope.Cancellable() }

    /**
     * Called before an item gets dropped by a player
     */
    val preItemDrop = Event.syncAsync<ServerPlayerItemEvent, EventScope.Cancellable> { EventScope.Cancellable() }

}