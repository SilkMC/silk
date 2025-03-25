package net.silkmc.silk.test.commands

import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.silkmc.silk.core.annotations.DelicateSilkApi
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.logging.logInfo
import net.silkmc.silk.core.text.literalText
import net.silkmc.silk.core.text.sendText
import net.silkmc.silk.core.text.serializeToPrettyJson

private val textExamples get() = mapOf(
    "simple" to literalText("Hello, World!"),
    "simple_styled" to literalText("Hello, World!") {
        bold = true
        color = 0xFF5E13
    },
    "welcome" to literalText {
        text {
            color = 0xFF5E13
            text("Welcome on ")
            text("our") {
                underline = true
            }
            text(" Server") {
                color = 0x21FF9E
            }
            text("!")
        }
        text(" ")
        text {
            color = 0xFDFF80
            hoverEvent = HoverEvent.ShowText(literalText("nice, you did it") {
                bold = true
                color = 0x39CEFF
            })
            text("[")
            text("hover and click here") {
                color = 0xFFE12C
                clickEvent = ClickEvent.RunCommand("/tellraw @s \"hey :)\"")
            }
            text("]")
        }
    },
    "embedded_text" to literalText {
        text("----------------")
        newLine()
        color = 0x5FFFFF
        text(literalText("red: this is embedded") {
            newLine()
            text("green: this is nested in the embedded text") {
                color = 0x00FF00
                newLine()
                text("green: this is a child of the nested in embedded text")
            }
        }) {
            newLine()
            text("orange: this is a sibling builder of the embedded text") {
                color = 0xFFA500
            }
            newLine()
            text("red: this is a sibling builder of the embedded text")
            color = 0xFF0000
        }
        newLine()
        text("cyan: this is not embedded")
        newLine()
        text(literalText("cyan: this is embedded unstyled") {
            newLine()
            text("cyan: this is a child of embedded unstyled")
        }) {
            newLine()
            text("cyan: this is a sibling builder of embedded unstyled")
        }
        newLine()
        text(literalText("white: this is an embedded text without inheritance") {
            newLine()
            text("white: this is a child of the embedded text without inheritance")
        }, inheritStyle = false)
        newLine()
        text("white: this is not embedded and without inheritance", inheritStyle = false)
        newLine()
        text("pink: this is a basic pink styled text") {
            color = 0xFF00FF
        }
    }
)

@OptIn(ExperimentalSilkApi::class)
val textTestCommand = testCommand("text") {
    argument<String>("textexample") { example ->
        suggestList { textExamples.keys }
        runs {
            val exampleText = textExamples[example()] ?: return@runs
            source.playerOrException.sendText(exampleText)

            @OptIn(DelicateSilkApi::class)
            logInfo("The json string for the printed text is:\n${exampleText.serializeToPrettyJson()}")
        }
    }
}
