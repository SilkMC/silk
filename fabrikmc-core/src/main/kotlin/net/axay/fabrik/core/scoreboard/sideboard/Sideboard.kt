package net.axay.fabrik.core.scoreboard.sideboard

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.minecraft.scoreboard.Scoreboard
import net.minecraft.scoreboard.ScoreboardCriterion
import net.minecraft.scoreboard.ServerScoreboard
import net.minecraft.text.Text

class Sideboard(
    name: String,
    displayName: Text,
    lines: List<SideboardLine>,
) {
    companion object {
        val sidebarId = Scoreboard.getDisplaySlotId("sidebar")
    }

    private val scoreboard = ServerScoreboard(Fabrik.currentServer)
    private val objective = scoreboard.addObjective(name, ScoreboardCriterion.DUMMY, displayName, ScoreboardCriterion.RenderType.INTEGER)

    init {
        scoreboard.setObjectiveSlot(sidebarId, objective)

        lines.forEachIndexed { index, line ->
            val team = scoreboard.addTeam("team_$index")
            scoreboard.addPlayerToTeam("§$index", team)
            scoreboard.getPlayerScore("§$index", objective).score = lines.size - index

            fabrikCoroutineScope.launch {
                line.textFlow.collect {
                    team.prefix = it
                }
            }
        }
    }
}
