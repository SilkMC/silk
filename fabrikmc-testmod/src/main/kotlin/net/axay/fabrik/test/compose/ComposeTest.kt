package net.axay.fabrik.test.compose

import androidx.compose.foundation.layout.Column
import net.axay.fabrik.compose.displayComposable
import net.axay.fabrik.compose.ui.McWindowHeader
import net.axay.fabrik.test.commands.testCommand
import net.axay.fabrik.test.compose.game.FallingBallsGameComposable
import net.axay.fabrik.test.compose.guis.ScrollTestComposable
import net.axay.fabrik.test.compose.guis.GeneralTestComposable

val composeCommand = testCommand("compose") {
    literal("general_test") runs {
        source.playerOrException.displayComposable(4, 3) {
            Column {
                McWindowHeader(it, "Test Window")
                GeneralTestComposable()
            }
        }
    }

    literal("falling_balls") runs {
        source.playerOrException.displayComposable(8, 6) {
            McWindowHeader(it)
            FallingBallsGameComposable()
        }
    }

    literal("scroll") runs {
        source.playerOrException.displayComposable(5, 4) {
            ScrollTestComposable()
        }
    }
}
