package net.axay.fabrik.test.compose

import androidx.compose.foundation.layout.Column
import net.axay.fabrik.compose.displayComposable
import net.axay.fabrik.compose.ui.McWindowHeader
import net.axay.fabrik.test.commands.testCommand
import net.axay.fabrik.test.compose.game.FallingBallsGameComposable
import net.axay.fabrik.test.compose.guis.TestGuiComposable

val composeCommand = testCommand("compose") {
    literal("testcomposable") runs {
        source.player.displayComposable(4, 3) {
            Column {
                McWindowHeader(it, "Test Window")
                TestGuiComposable()
            }
        }
    }

    literal("falling_balls") runs {
        source.player.displayComposable(8, 6) {
            McWindowHeader(it)
            FallingBallsGameComposable()
        }
    }
}
