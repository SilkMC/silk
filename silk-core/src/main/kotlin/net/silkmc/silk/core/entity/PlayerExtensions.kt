package net.silkmc.silk.core.entity

import net.minecraft.server.level.ServerPlayer

/**
 * Executes the given [command] for this player.
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun ServerPlayer.executeCommand(command: String) {
    server.commands.performCommand(server.commands.dispatcher.parse(command, createCommandSourceStack()), command)
}
