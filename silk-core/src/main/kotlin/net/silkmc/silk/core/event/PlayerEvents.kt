package net.silkmc.silk.core.event

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Event
import net.silkmc.silk.core.event.EventScope
import net.silkmc.silk.core.event.Events

@Suppress("UnusedReceiverParameter") // receiver is for namespacing only
val Events.Player get() = PlayerEvents

@ExperimentalSilkApi
object PlayerEvents {

    open class ServerPlayerEvent(val player: ServerPlayer)

    /**
     * Called before a player receives the login packet from the server.
     */
    val preLogin = Event.syncAsyncImmutable<ServerPlayerEvent>()

    /**
     * Called after a player has received all login information from the server.
     */
    val postLogin = Event.syncAsyncImmutable<ServerPlayerEvent>()


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
