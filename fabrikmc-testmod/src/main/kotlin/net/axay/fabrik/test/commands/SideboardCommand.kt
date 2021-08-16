package net.axay.fabrik.test.commands

import net.axay.fabrik.commands.argument
import net.axay.fabrik.commands.command
import net.axay.fabrik.commands.simpleExecutes
import net.axay.fabrik.commands.simpleSuggests
import net.axay.fabrik.core.scoreboard.sideboard.showSideboard
import net.axay.fabrik.core.scoreboard.sideboard.sideboard
import net.axay.fabrik.core.text.literalText

val sideboardCommand = command("sideboard", true) {
    argument<String>("example") {
        simpleSuggests { sideboardExamples.keys }
        simpleExecutes {
            val sideboard = sideboardExamples[resolveArgument()] ?: return@simpleExecutes
            source.player.showSideboard(sideboard)
        }
    }
}

private val sideboardExamples = mapOf(
    "simple" to sideboard(
        literalText("Simple Sideboard") { color = 0x6DFF41 }
    ) {
        literalLine("Hey wie,")
        literalLine("geht's?")
        literalLine(" ")
        lineChangingPeriodically(1000) {
            literalText("Farben gehen auch") {
                color = (0x000000..0xFFFFFF).random()
            }
        }
    }
)
