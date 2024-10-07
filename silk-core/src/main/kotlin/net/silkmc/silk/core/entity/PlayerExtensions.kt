package net.silkmc.silk.core.entity

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.silkmc.silk.core.server.executeCommand

/**
 * Executes the given [command] for this player.
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun Player.executeCommand(command: String) {
    val serverPlayer = this as? ServerPlayer
    serverPlayer?.server?.executeCommand(command, serverPlayer.createCommandSourceStack())
}
