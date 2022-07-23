package net.silkmc.silk.core.event

import net.minecraft.network.Connection
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

/**
 * Events related to an [ServerPlayer].
 */
@Suppress("unused") // receiver is for namespacing only
val Events.PlayerConnection get() = PlayerConnectionEvents

@ExperimentalSilkApi
object PlayerConnectionEvents {

    open class PlayerConnectionEvent(val server: ServerPlayer, val connection: Connection)
    class PlayerJoinEvent()
}
