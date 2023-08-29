package net.silkmc.silk.core.event

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@ExperimentalSilkApi
object PlayerEvents {

    open class PlayerEvent<T : Player?>(val player: T)

    /**
     * Called before a player receives the login packet from the server.
     */
    val preLogin = Event.syncAsync<PlayerEvent<ServerPlayer>>()

    /**
     * Called after a player has received all login information from the server.
     */
    val postLogin = Event.syncAsync<PlayerEvent<ServerPlayer>>()

    /**
     * Called before a player leaves the server.
     */
    val preQuit = Event.syncAsync<PlayerEvent<ServerPlayer>>()

    /**
     * Called when a player disconnects during the login process.
     */
    val quitDuringLogin = Event.syncAsync<PlayerEvent<ServerPlayer?>>()
}
