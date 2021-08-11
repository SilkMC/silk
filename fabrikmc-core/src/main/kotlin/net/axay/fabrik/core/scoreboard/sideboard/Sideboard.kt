package net.axay.fabrik.core.scoreboard.sideboard

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.axay.fabrik.core.scoreboard.FabrikSideboardScoreboard
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.minecraft.text.Text

class Sideboard(
    name: String,
    displayName: Text,
    lines: List<SideboardLine>,
) {
    internal val scoreboard = FabrikSideboardScoreboard(name, displayName)

    init {
        lines.forEachIndexed { index, line ->
            val team = scoreboard.addTeam("team_$index")
            scoreboard.addPlayerToTeam("§$index", team)
            scoreboard.setPlayerScore("§$index", lines.size - index)

            fabrikCoroutineScope.launch {
                line.textFlow.collect {
                    team.prefix = it
                }
            }
        }
    }
}
