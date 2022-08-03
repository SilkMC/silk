package net.silkmc.silk.core.event.player

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
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
val Events.ServerPlayer get() = ServerPlayerEvents

object ServerPlayerEvents {

    @Suppress("unused")
    open class ServerPlayerEvent(player: ServerPlayer) : PlayerEvents.PlayerEvent(player)


}