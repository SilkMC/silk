package net.silkmc.silk.core.entity

import net.minecraft.world.entity.player.Player

/**
 * Executes the given [command] for this player.
 */
fun Player.executeCommand(command: String) {
    server?.commands?.performCommand(createCommandSourceStack(), command)
}
