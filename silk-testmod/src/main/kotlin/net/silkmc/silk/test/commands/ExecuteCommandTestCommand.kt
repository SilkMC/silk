package net.silkmc.silk.test.commands

import net.silkmc.silk.commands.command
import net.silkmc.silk.core.entity.executeCommand
import net.silkmc.silk.core.server.executeCommand

val executeCommandTestCommand = command("executeCommand") {
    literal("server") {
        runs {
            val server = this.source.server
            server?.executeCommand("time set day")
        }
    }
    literal("player") {
        runs {
            val player = this.source.player
            player?.executeCommand("summon pig")
        }
    }
}