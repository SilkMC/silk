package net.axay.fabrik.core.scoreboard.sideboard

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.logging.logInfo
import net.axay.fabrik.core.scoreboard.FabrikSideboardScoreboard
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.axay.fabrik.core.task.initWithServerAsync
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class Sideboard(
    name: String,
    displayName: Text,
    lines: List<SideboardLine>,
) {
    private val scoreboardDeferred = initWithServerAsync { FabrikSideboardScoreboard(name, displayName) }

    init {
        fabrikCoroutineScope.launch {
            val scoreboard = scoreboardDeferred.await()

            lines.forEachIndexed { index, line ->
                val team = scoreboard.addTeam("team_$index")
                scoreboard.addPlayerToTeam("§$index", team)
                scoreboard.setPlayerScore("§$index", lines.size - index)

                fabrikCoroutineScope.launch {
                    line.textFlow.collect {
                        logInfo("setting ${team.name} prefix to '${it.string}'")
                        team.prefix = it
                    }
                }
            }
        }
    }

    internal fun displayToPlayer(player: ServerPlayerEntity) = fabrikCoroutineScope.launch {
        if (Fabrik.currentServer?.isRunning == true)
            scoreboardDeferred.await().displayToPlayer(player)
    }
}
