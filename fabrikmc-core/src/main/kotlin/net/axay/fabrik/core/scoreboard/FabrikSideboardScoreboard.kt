package net.axay.fabrik.core.scoreboard

import net.axay.fabrik.core.packet.sendPacket
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.TeamS2CPacket
import net.minecraft.scoreboard.*
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

/**
 * A server side scoreboard which is only displayed to
 * a selected collection of players.
 * This implementation is used by the [net.axay.fabrik.core.scoreboard.sideboard.Sideboard]
 * api.
 *
 * The packet implementation of this class is not completed, thus
 * it is internal for now.
 *
 * Have a look at [net.minecraft.scoreboard.ServerScoreboard] for a
 * complete packet implementation.
 */
internal class FabrikSideboardScoreboard(
    name: String,
    displayName: Text,
) : Scoreboard() {
    companion object {
        val sidebarId = getDisplaySlotId("sidebar")
    }

    private val players = HashSet<ServerPlayerEntity>()

    private val dummyObjective =
        addObjective(name, ScoreboardCriterion.DUMMY, displayName, ScoreboardCriterion.RenderType.INTEGER)

    // initialize the objective slot of the dummy objective
    init {
        setObjectiveSlot(sidebarId, dummyObjective)

        val updatePackets = ArrayList<Packet<*>>()
        updatePackets += ScoreboardObjectiveUpdateS2CPacket(dummyObjective, ScoreboardObjectiveUpdateS2CPacket.ADD_MODE)
        updatePackets += ScoreboardDisplayS2CPacket(sidebarId, dummyObjective)
        // TODO: the following is probably not needed
        updatePackets += getAllPlayerScores(dummyObjective).map {
            ScoreboardPlayerUpdateS2CPacket(
                ServerScoreboard.UpdateMode.CHANGE, it.objective!!.name, it.playerName, it.score
            )
        }

        updatePackets.forEach(players::sendPacket)
    }

    fun setPlayerScore(player: String, score: Int) {
        getPlayerScore(player, dummyObjective).score = score
    }

    override fun updateScore(score: ScoreboardPlayerScore) {
        super.updateScore(score)

        if (dummyObjective == score.objective) {
            players.sendPacket(
                ScoreboardPlayerUpdateS2CPacket(
                    ServerScoreboard.UpdateMode.CHANGE, score.objective!!.name, score.playerName, score.score
                )
            )
        }
    }

    override fun addPlayerToTeam(playerName: String, team: Team): Boolean {
        return if (super.addPlayerToTeam(playerName, team)) {
            players.sendPacket(TeamS2CPacket.changePlayerTeam(team, playerName, TeamS2CPacket.Operation.ADD))
            true
        } else false
    }

    override fun removePlayerFromTeam(playerName: String, team: Team) {
        super.removePlayerFromTeam(playerName, team)
        players.sendPacket(TeamS2CPacket.changePlayerTeam(team, playerName, TeamS2CPacket.Operation.REMOVE))
    }

    override fun updateScoreboardTeam(team: Team) {
        super.updateScoreboardTeam(team)
        players.sendPacket(TeamS2CPacket.updateTeam(team, false))
    }
}
