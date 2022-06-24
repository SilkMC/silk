package net.silkmc.silk.core.entity

import net.minecraft.world.entity.player.Player

/**
 * Executes the given [command] for this player.
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun Player.executeCommand(command: String) {
    server?.commands?.performCommand(createCommandSourceStack(), command)
}
