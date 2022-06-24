package net.silkmc.silk.test.commands

import net.silkmc.silk.core.text.literalText
import net.silkmc.silk.game.sideboard.sideboard

val sideboardCommand = testCommand("sideboard") {
    argument("example") { example ->
        suggestList { sideboardExamples.keys }
        literal("display") {
            runs {
                sideboardExamples[example()]?.displayToPlayer(source.playerOrException)
            }
        }
        literal("hide") {
            runs {
                sideboardExamples[example()]?.hideFromPlayer(source.playerOrException)
            }
        }
    }
}

private val sideboardExamples = mapOf(
    "simple" to sideboard(
        literalText("Simple Sideboard") { color = 0x6DFF41 }
    ) {
        literalLine("Hey, how")
        literalLine("are you?")
        literalLine(" ")
        literalLine("colors work as well!") {
            color = 0xFF9658
        }
    },

    "simple_changing" to sideboard(
        literalText("Simple Sideboard") { color = 0x6DFF41 }
    ) {
        literalLine("Hey, how")
        literalLine("are you?")
        literalLine(" ")
        lineChangingPeriodically(1000) {
            literalText("changing color") {
                color = (0x000000..0xFFFFFF).random()
            }
        }
    }
)
