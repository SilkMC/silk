package net.silkmc.silk.core.event

import net.minecraft.server.MinecraftServer
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@ExperimentalSilkApi
object ServerEvents {

    open class ServerEvent(val server: MinecraftServer)

    /**
     * Called before the server is initialized.
     */
    val preStart = Event.syncAsync<ServerEvent>()

    /**
     * Called after the server has successfully started.
     */
    val postStart = Event.syncAsync<ServerEvent>()

    /**
     * Called before the server begins to shut down.
     */
    val preStop = Event.syncAsync<ServerEvent>()

    /**
     * Called after the server has successfully stopped.
     */
    val postStop = Event.syncAsync<ServerEvent>()

    class PreHaltEvent(server: MinecraftServer) : ServerEvent(server), Cancellable {
        override val isCancelled = EventScopeProperty(false)
    }

    /**
     * Called before the game loop is stopped (running set to false).
     */
    val preHalt = Event.syncAsync<PreHaltEvent>()
}
