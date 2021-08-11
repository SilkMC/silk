package net.axay.fabrik.test.commands

import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.fabrik.commands.*
import net.axay.fabrik.core.scoreboard.sideboard.showSideboard
import net.axay.fabrik.core.scoreboard.sideboard.sideboard
import net.axay.fabrik.core.text.literalText

val sideboardCommand = command("sideboard", true) {
    argument("example", StringArgumentType.string()) {
        simpleSuggests { sideboardExamples.keys }
        simpleExecutes {
            val sideboard = sideboardExamples[it.getArgument("example")] ?: return@simpleExecutes
            it.source.player.showSideboard(sideboard)
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
