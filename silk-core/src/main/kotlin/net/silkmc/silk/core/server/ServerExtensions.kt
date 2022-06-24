package net.silkmc.silk.core.server

import net.minecraft.server.MinecraftServer

/**
 * Executes the given [command] for this server.
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun MinecraftServer.executeCommand(command: String) {
    commands.performCommand(createCommandSourceStack(), command)
}
