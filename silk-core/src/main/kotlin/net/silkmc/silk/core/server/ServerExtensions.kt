package net.silkmc.silk.core.server

import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer

/**
 * Executes the given [command] for this server.
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun MinecraftServer.executeCommand(command: String) {
    executeCommand(command, this.createCommandSourceStack())
}

/**
 * Executes the given [command] for the specified [source].
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun MinecraftServer.executeCommand(command: String, source: CommandSourceStack) {
    val parsed = commands.dispatcher.parse(command, source)
    commands.performCommand(parsed, command)
}
