package net.silkmc.silk.core.event

import com.mojang.authlib.GameProfile
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@ExperimentalSilkApi
object PlayerEvents {

    open class PlayerEvent<T : Player?>(val player: T)

    /**
     * Called before a player receives the login packet from the server.
     */
    val preLogin = Event.syncAsync<PlayerEvent<ServerPlayer>>()

    open class PostLoginEvent(
        player: ServerPlayer,
        val joinMessage: EventScopeProperty<Component>
    ) : PlayerEvent<ServerPlayer>(player)

    /**
     * Called after a player has received all login information from the server.
     */
    val postLogin = Event.syncAsync<PostLoginEvent>()

    open class PlayerQuitEvent(
        player: ServerPlayer,
        val reason: Component,
    ) : PlayerEvent<ServerPlayer>(player)

    /**
     * Called before a player leaves the server. Will only be called when
     * a player who was fully in-game leaves the server.
     *
     * @see quitDuringLogin
     * @see quitDuringConfiguration
     */
    val preQuit = Event.syncAsync<PlayerQuitEvent>()

    open class PlayerQuitDuringLoginEvent(
        val gameProfile: GameProfile?,
        val reason: Component,
    )

    /**
     * Called when a player disconnects during the login process.
     *
     * Note: this only applies to the early login phase - for the
     * configuration phase (between login and actual game join) see
     * [quitDuringConfiguration]
     *
     * @see quitDuringConfiguration
     * @see preQuit
     */
    val quitDuringLogin = Event.syncAsync<PlayerQuitDuringLoginEvent>()

    /**
     * Called when a player disconnects in the configuration phase. This is
     * the phase between login and actual game join.
     *
     * @see quitDuringLogin
     * @see preQuit
     */
    val quitDuringConfiguration = Event.syncAsync<PlayerQuitDuringLoginEvent>()

    open class PlayerDeathEvent(
        player: ServerPlayer,
        val source: DamageSource,
        var deathMessage: EventScopeProperty<Component>,
    ) : PlayerEvent<ServerPlayer>(player)

    /**
     * Called when a [ServerPlayer] dies.
     */
    val onDeath = Event.syncAsync<PlayerDeathEvent>()


}
