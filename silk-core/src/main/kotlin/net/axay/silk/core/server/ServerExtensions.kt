package net.axay.silk.core.server

import net.minecraft.server.MinecraftServer

/**
 * Executes the given [command] for this server.
 */

fun MinecraftServer.executeCommand(command: String){
    this.commands.performCommand(this.createCommandSourceStack(), command)
}