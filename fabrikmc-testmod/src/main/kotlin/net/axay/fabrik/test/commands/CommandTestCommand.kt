package net.axay.fabrik.test.commands

import net.axay.fabrik.core.text.sendText

val commandTestCommand = testCommand("commandtest") {
    runs {
        source.playerOrException.sendText("Top Level 'runs'")
    }
    argument<Float>("testarg") {
        runs {
            source.playerOrException.sendText("'runs' inside 'testarg' got the following argument: ${it()}")
        }
    }
}
