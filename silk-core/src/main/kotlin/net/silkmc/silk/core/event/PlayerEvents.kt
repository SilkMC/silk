package net.silkmc.silk.core.event

import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

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
}
