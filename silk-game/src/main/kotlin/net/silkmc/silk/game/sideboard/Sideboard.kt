package net.silkmc.silk.game.sideboard

import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.task.initWithServerAsync
import net.silkmc.silk.core.task.silkCoroutineScope
import net.silkmc.silk.game.scoreboard.ScoreboardLine
import net.silkmc.silk.game.sideboard.internal.SideboardScoreboard

/**
 * A sideboard which can be displayed to a variable collection of players
 * using the [displayToPlayer] function. A sideboard is an abstraction of Mincraft's
 * server side scoreboards displayed on the right-hand side of the screen.
 *
 * **Note:** You probably want to build this class using the sideboard builder API. See [sideboard]!
 */
class Sideboard(
    name: String,
    displayName: Component,
    lines: List<ScoreboardLine>,
) {
    @InternalSilkApi
    val scoreboardDeferred = initWithServerAsync {
        SideboardScoreboard(name, displayName).also { scoreboard ->
            lines.forEachIndexed { index, line ->
                val team = scoreboard.addPlayerTeam("team_$index")
                scoreboard.addPlayerToTeam("§$index", team)
                scoreboard.setPlayerScore("§$index", lines.size - index)

                silkCoroutineScope.launch {
                    line.textFlow.collect {
                        team.playerPrefix = it
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
        if (Silk.currentServer?.isRunning == true)
            silkCoroutineScope.launch {
                scoreboardDeferred.await().displayToPlayer(player)
            }
    }

    /**
     * Hides this sideboard from the given [player]. This also unregisters the
     * player from receiving any further updates on the sideboard.
     */
    fun hideFromPlayer(player: ServerPlayer) {
        if (Silk.currentServer != null)
            silkCoroutineScope.launch {
                scoreboardDeferred.await().hideFromPlayer(player)
            }
    }
}
