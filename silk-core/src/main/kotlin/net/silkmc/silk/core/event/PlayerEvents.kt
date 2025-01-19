package net.silkmc.silk.core.event

import com.mojang.authlib.GameProfile
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
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

    open class PlayerDeathMessageEvent(
        player: ServerPlayer,
        val deathMessage: EventScopeProperty<Component>,
    ): PlayerEvent<ServerPlayer>(player)

    /**
     * Called when a player dies and sends a death message. This event will not trigger if death messages are disabled.
     */
    val deathMessage = Event.syncAsync<PlayerDeathMessageEvent>()

    open class PlayerBlockBreakEvent(
        player: Player,
        val blockPos: BlockPos,
        val blockState: BlockState,
        val blockEntity: BlockEntity?,
        val isCancelled: EventScopeProperty<Boolean>,
    ): PlayerEvent<Player>(player)

    /**
     * Called when a player breaks a block. This event is triggered before the block is broken.
     */
    val blockBreak = Event.syncAsync<PlayerBlockBreakEvent>()
}
