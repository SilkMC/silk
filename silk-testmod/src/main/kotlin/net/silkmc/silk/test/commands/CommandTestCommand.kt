package net.silkmc.silk.test.commands

import net.silkmc.silk.core.text.sendText

val commandTestCommand = testCommand("commandtest") {
    runs {
        source.playerOrException.sendText("top Level 'runs'")
    }
    argument<Float>("testarg") {
        runs {
            source.playerOrException.sendText("'runs' inside 'testarg' got the following argument: ${it()}")
        }
    }

    literal("literal_test") {
        literal("testliteral_base") {
            alias("testliteral_alias")
            runs {
                source.playerOrException.sendText("'runs' inside 'testliteral'")
            }
        }
    }
}
