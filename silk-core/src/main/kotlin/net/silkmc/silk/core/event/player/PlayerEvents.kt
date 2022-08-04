package net.silkmc.silk.core.event.player

import net.minecraft.advancements.Advancement
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Event
import net.silkmc.silk.core.event.EventScope
import net.silkmc.silk.core.event.Events

/**
 * Events related to a [net.minecraft.world.entity.player.Player]
 */
@Suppress("unused")
val Events.Player get() = PlayerEvents

@ExperimentalSilkApi
object PlayerEvents {

    @Suppress("unused")
    open class PlayerEvent(val player: Player)

}

/**
 * Events related to a [net.minecraft.server.level.ServerPlayer]
 */
@Suppress("unused")
val Events.ServerPlayer get() = ServerPlayerEvents

object ServerPlayerEvents {

    @Suppress("unused")
    open class ServerPlayerEvent(player: ServerPlayer) : PlayerEvents.PlayerEvent(player)

    class AdvancementEvent(player: ServerPlayer, val advancement: Advancement, var message: Component) :
        ServerPlayerEvent(player)

    /**
     * Called before an advancement message gets sent
     *
     * If the event is cancelled no message will be sent!
     */
    val onAdvancement = Event<AdvancementEvent, EventScope.Cancellable> { EventScope.Cancellable() }

}