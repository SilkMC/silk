package net.silkmc.silk.test.commands

import net.silkmc.silk.core.entity.executeCommand
import net.silkmc.silk.core.server.executeCommand

val executeCommandTestCommand = testCommand("execute_command") {
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
