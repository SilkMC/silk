package net.silkmc.silk.test.commands

import kotlinx.coroutines.flow.flowOf
import net.silkmc.silk.core.text.literalText
import net.silkmc.silk.game.scoreboard.ScoreboardLine
import net.silkmc.silk.game.tablist.tablist
import java.util.*
import kotlin.time.Duration.Companion.seconds

val tablistCommand = testCommand("tablist") {
    literal("updating") {
        runs {
            updatingTablistLine.launchUpdate(literalText((1 .. 600000).random().toString()) {
                color = (0x000000..0xFFFFFF).random()
            })
        }
    }
    literal("updatePlayers") {
        runs {
            tablist.updateNames()
        }
    }
}


val updatingTablistLine = ScoreboardLine.Updatable(literalText("Starting string"))

val tablist = tablist {

    generateName {
        literalText("${this.name.string}sad") {
            color = (0x44212 .. 0x45641).random()
        }
    }

    footer(
        listOf(
            updatingTablistLine,
            ScoreboardLine.UpdatingPeriodically(1.seconds) {
                literalText(UUID.randomUUID().toString()) {
                    color = 0x568F97
                }
            })
    )

    header(
        listOf(
            ScoreboardLine.Static(literalText("Some static text") { color = 0x200D97 })
        )
    )
}