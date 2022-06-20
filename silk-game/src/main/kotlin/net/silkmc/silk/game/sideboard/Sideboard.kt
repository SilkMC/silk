package net.silkmc.silk.game.sideboard

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.task.initWithServerAsync
import net.silkmc.silk.core.task.silkCoroutineScope
import net.silkmc.silk.game.sideboard.internal.SilkSideboardScoreboard

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
    lines: List<SideboardLine>,
) {
    private val scoreboardDeferred = initWithServerAsync { SilkSideboardScoreboard(name, displayName) }

    private val initLock = CompletableDeferred<Boolean>()

    init {
        silkCoroutineScope.launch {
            val scoreboard = scoreboardDeferred.await()

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

            initLock.complete(true)
        }
    }

    internal fun displayToPlayer(player: ServerPlayer) {
        if (Silk.currentServer?.isRunning == true)
            silkCoroutineScope.launch {
                initLock.await()
                scoreboardDeferred.await().displayToPlayer(player)
            }
    }
}
