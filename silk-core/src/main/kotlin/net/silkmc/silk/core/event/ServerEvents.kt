package net.silkmc.silk.core.event

import net.minecraft.server.MinecraftServer
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@ExperimentalSilkApi
object ServerEvents {

    class ServerEvent(val server: MinecraftServer)

    /**
     * Called before the server is initialized.
     */
    val preStart = Event.syncAsyncImmutable<ServerEvent>()

    /**
     * Called after the server has successfully started.
     */
    val postStart = Event.syncAsyncImmutable<ServerEvent>()

    /**
     * Called before the server begins to shut down.
     */
    val preStop = Event.syncAsyncImmutable<ServerEvent>()

    /**
     * Called after the server has successfully stopped.
     */
    val postStop = Event.syncAsyncImmutable<ServerEvent>()

    /**
     * Called before the game loop is stopped (running set to false).
     */
    val preHalt = Event.syncAsync<ServerEvent, EventScope.Cancellable> {
        EventScope.Cancellable()
    }
}
