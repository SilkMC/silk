package net.silkmc.silk.core.event

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

/**
 * Events related to an [ServerPlayer].
 */
@Suppress("unused") // receiver is for namespacing only
val Events.PlayerConnection get() = PlayerConnectionEvents

@ExperimentalSilkApi
object PlayerConnectionEvents {

    class PlayerJoinEvent(val player: ServerPlayer, var joinMessage: Component)
    class PlayerQuitEvent(val player: ServerPlayer, var quitMessage: Component)

    /**
     * Called when a player joins the server
     */
    val playerJoin = Event.syncAsync<PlayerJoinEvent, EventScope.Empty> { EventScope.Empty }

    /**
     * Called when a player leaves the server
     */
    val playerQuit = Event.syncAsync<PlayerQuitEvent, EventScope.Empty> { EventScope.Empty }
}
