package net.silkmc.silk.test.commands

import net.silkmc.silk.core.text.literal
import net.silkmc.silk.core.text.literalText
import net.silkmc.silk.game.scoreboard.ScoreboardLine
import net.silkmc.silk.game.sideboard.sideboard
import kotlin.time.Duration.Companion.seconds

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
        literal("update_updatable") {
            argument<String>("content") { contentArg ->
                runsAsync {
                    updatableLine.update(contentArg().literal)
                }
            }
        }
    }
}

private val updatableLine = ScoreboardLine.Updatable()

private val sideboardExamples = mapOf("simple" to sideboard(literalText("Simple Sideboard") { color = 0x6DFF41 }) {
    line("Hey, how".literal)
    line("are you?".literal)
    emptyLine()
    line(literalText("colors work as well!") {
        color = 0xFF9658
    })
},

    "simple_changing" to sideboard(literalText("Changing Sideboard") { color = 0x6DFF41 }) {
        line("Hey, how".literal)
        line("are you?".literal)
        emptyLine()
        updatingLine(1.seconds) {
            literalText("changing color") {
                color = (0x000000..0xFFFFFF).random()
            }
        }
    },

    "changing_externally" to sideboard(literalText("External Changes") { color = 0x3BFF88 }) {
        line("The following line".literal)
        line("is updatable:".literal)
        line(updatableLine)
    })
