package net.axay.fabrik.core.scoreboard

import net.axay.fabrik.core.Fabrik
import net.minecraft.scoreboard.Scoreboard
import net.minecraft.scoreboard.ScoreboardCriterion
import net.minecraft.scoreboard.ServerScoreboard
import net.minecraft.text.Text

class Sideboard(
    name: String,
    displayName: Text,
    lines: List<Text>,
) {
    companion object {
        val sidebarId = Scoreboard.getDisplaySlotId("sidebar")
    }

    private val scoreboard = ServerScoreboard(Fabrik.currentServer)
    private val objective = scoreboard.addObjective(name, ScoreboardCriterion.DUMMY, displayName, ScoreboardCriterion.RenderType.INTEGER)

    init {
        lines.forEachIndexed { index, line ->
            val team = scoreboard.addTeam("team_$index")
            team.prefix = line
            scoreboard.addPlayerToTeam("§$index", team)
            scoreboard.getPlayerScore("§$index", objective).score = lines.size - index
        }

        scoreboard.setObjectiveSlot(sidebarId, objective)
    }
}
