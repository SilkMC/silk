package net.axay.fabrik.test.commands

import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.fabrik.commands.*
import net.axay.fabrik.core.scoreboard.Sideboard
import net.axay.fabrik.core.text.literal
import net.axay.fabrik.core.text.literalText

val sideboardCommand = command("sideboard", true) {
    argument("example", StringArgumentType.string()) {
        simpleSuggests { sideboardExamples.keys }
        simpleExecutes {
            sideboardExamples[it.getArgument("example")]?.value
        }
    }
}

private val sideboardExamples = mapOf(
    "simple" to lazy {
        Sideboard(
            "simple",
            literalText("Simple Sideboard") {
                color = 0x6DFF41
            },
            listOf(
                "Hey wie,".literal,
                "geht's?".literal,
                " ".literal,
                literalText("Farben gehen auch") {
                    color = 0x57FFF0
                }
            )
        )
    }
)
