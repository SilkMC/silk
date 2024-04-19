package net.silkmc.silk.game.sideboard

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.numbers.NumberFormat
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.task.silkCoroutineScope
import net.silkmc.silk.game.sideboard.internal.SideboardScoreboard

/**
 * A sideboard which can be displayed to a variable collection of players
 * using the [displayToPlayer] function. A sideboard is an abstraction of Minecraft's
 * server side scoreboards displayed on the right-hand side of the screen.
 *
 * **Note:** You probably want to build this class using the sideboard builder API. See [sideboard]!
 */
class Sideboard(
    name: String,
    displayName: Component,
    numberFormat: NumberFormat,
    lines: List<SideboardLine>,
) {

    @InternalSilkApi
    val scoreboard = SideboardScoreboard(name, displayName, numberFormat).also { scoreboard ->
        @OptIn(ExperimentalCoroutinesApi::class)
        silkCoroutineScope.launch(Dispatchers.Default.limitedParallelism(1)) {
            lines.forEach { line ->
                val scoreboardLine = scoreboard.addLine(line.textFlow.first())
                silkCoroutineScope.launch {
                    line.textFlow.collect {
                        scoreboardLine.setContent(it)
                    }
                }
            }
        }
    }

    /**
     * Shows this sideboard to the given [player]. This registers the player
     * for receiving updates on the sideboard, until he leaves the server.
     */
    fun displayToPlayer(player: ServerPlayer) {
        silkCoroutineScope.launch {
            scoreboard.displayToPlayer(player)
        }
    }

    /**
     * Hides this sideboard from the given [player]. This also unregisters the
     * player from receiving any further updates on the sideboard.
     */
    fun hideFromPlayer(player: ServerPlayer) {
        silkCoroutineScope.launch {
            scoreboard.hideFromPlayer(player)
        }
    }
}
