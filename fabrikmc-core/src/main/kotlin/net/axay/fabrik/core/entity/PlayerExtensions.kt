package net.axay.fabrik.core.entity

import net.axay.fabrik.core.Fabrik
import net.minecraft.world.entity.player.Player

/**
 * Executes the given [command] for this player.
 */
fun Player.executeCommand(command: String) {
    Fabrik.currentServer?.commands?.performCommand(createCommandSourceStack(), command)
}
