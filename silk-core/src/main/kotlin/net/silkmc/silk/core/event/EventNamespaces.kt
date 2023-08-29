@file:Suppress("UnusedReceiverParameter") // receivers are for namespacing only

package net.silkmc.silk.core.event

import net.silkmc.silk.core.annotations.ExperimentalSilkApi

/**
 * The base object for discovering events, check the extensions
 * values of this object.
 */
@ExperimentalSilkApi
object Events

/**
 * Events related to the [net.minecraft.server.MinecraftServer].
 */
val Events.Server get() = ServerEvents

/**
 * Events for actions specifically related to [net.minecraft.world.entity.player.Player].
 * For more general events, see [Events.Entity].
 */
val Events.Player get() = PlayerEvents

/**
 * Events for [net.minecraft.world.entity.Entity]. This includes players.
 */
val Events.Entity get() = EntityEvents
