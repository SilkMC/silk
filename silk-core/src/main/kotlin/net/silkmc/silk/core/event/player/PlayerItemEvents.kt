package net.silkmc.silk.core.event.player

import net.minecraft.server.level.ServerPlayer
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
     * Called before an item gets collected by a player
     */
    val itemCollect = Event.syncAsync<PlayerItemEvent, EventScope.Cancellable> { EventScope.Cancellable() }

    /**
     * Called before an item gets dropped by a player
     */
    val itemDrop = Event.syncAsync<PlayerItemEvent, EventScope.Cancellable> { EventScope.Cancellable() }

}

/**
 * Events related to Items and [net.minecraft.server.level.ServerPlayer]s
 */
val ServerPlayerEvents.Item get() = ServerPlayerEvents

object ServerPlayerItemEvents {

    class ServerPlayerItemEvent(val item: ItemStack, val level: Level, player: ServerPlayer) : ServerPlayerEvents.ServerPlayerEvent(player)

    /**
     * Called before an item gets dropped by a player
     */
    val itemDrop = Event.syncAsync<ServerPlayerItemEvent, EventScope.Cancellable> { EventScope.Cancellable() }

}