package net.silkmc.silk.core

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.server.players

@Deprecated(
    message = "FabrikMC has been renamed to Silk.",
    replaceWith = ReplaceWith("net.silkmc.silk.core.Silk")
)
typealias Fabrik = Silk

/**
 * An object containing global values used by Silk.
 */
object Silk {

    /**
     * The current [MinecraftServer] instance.
     * This property is `null` when being accessed
     * and no server has startet yet.
     */
    var server: MinecraftServer? = null
        internal set

    /**
     * The current [MinecraftServer] instance.
     * This property throws an [IllegalStateException] when being accessed
     * and no server has startet yet.
     */
    val serverOrThrow: MinecraftServer
        get() = server ?: error("Cannot get server, because none has been started yet")

    /**
     * Returns the list of current players, or an empty list if no
     * players are online or the server has not been started yet.
     *
     * @see [MinecraftServer.players]
     */
    val players: List<ServerPlayer>
        get() = server?.players.orEmpty()

    @Deprecated(
        message = "The property has been renamed to 'server'!",
        replaceWith = ReplaceWith("Silk.server")
    )
    val currentServer
        get() = server
}
