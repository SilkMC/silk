package net.axay.fabrik.test.compose

import net.axay.fabrik.compose.displayComposable
import net.axay.fabrik.test.commands.testCommand
import net.axay.fabrik.test.compose.game.FallingBallsGameComposable
import net.axay.fabrik.test.compose.guis.TestGuiComposable

val composeCommand = testCommand("compose") {
    literal("testcomposable") runs {
        source.player.displayComposable(4, 3) {
            TestGuiComposable()
        }
    }

    literal("falling_balls") runs {
        source.player.displayComposable(8, 6) {
            FallingBallsGameComposable()
        }
    }
}
