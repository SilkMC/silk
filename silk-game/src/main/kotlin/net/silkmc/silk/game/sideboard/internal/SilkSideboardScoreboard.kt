package net.silkmc.silk.game.sideboard.internal

import net.silkmc.silk.core.packet.sendPacket
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket
import net.minecraft.network.protocol.game.ClientboundSetScorePacket
import net.minecraft.server.ServerScoreboard
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.PlayerTeam
import net.minecraft.world.scores.Score
import net.minecraft.world.scores.Scoreboard
import net.minecraft.world.scores.criteria.ObjectiveCriteria

/**
 * A server side scoreboard which is only displayed to
 * a selected collection of players.
 * This implementation is used by the [net.silkmc.silk.core.sideboard.Sideboard]
 * api.
 *
 * The packet implementation of this class is not completed, thus
 * it is internal for now.
 *
 * Have a look at [net.minecraft.scoreboard.ServerScoreboard] for a
 * complete packet implementation.
 */
internal class SilkSideboardScoreboard(
    name: String,
    displayName: Component,
) : Scoreboard() {
    companion object {
        private val sidebarId = getDisplaySlotByName("sidebar")

        private val scoreboards = HashSet<SilkSideboardScoreboard>().also { boards ->
            ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
                boards.removeIf { it.players.remove(handler.player) && it.players.isEmpty() }
            }
        }
    }

    private val players = HashSet<ServerPlayer>()

    private val dummyObjective =
        addObjective(name, ObjectiveCriteria.DUMMY, displayName, ObjectiveCriteria.RenderType.INTEGER)

    init {
        setDisplayObjective(sidebarId, dummyObjective)
    }

    fun displayToPlayer(player: ServerPlayer) {
        players += player
        scoreboards.add(this)

        val updatePackets = ArrayList<Packet<*>>()

        updatePackets += ClientboundSetObjectivePacket(dummyObjective, ClientboundSetObjectivePacket.METHOD_ADD)
        updatePackets += ClientboundSetDisplayObjectivePacket(sidebarId, dummyObjective)
        getPlayerScores(dummyObjective).mapTo(updatePackets) {
            ClientboundSetScorePacket(
                ServerScoreboard.Method.CHANGE, it.objective!!.name, it.owner, it.score
            )
        }
        playerTeams.mapTo(updatePackets) { ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(it, true) }

        updatePackets.forEach(player.connection::send)
    }

    fun setPlayerScore(player: String, score: Int) {
        getOrCreatePlayerScore(player, dummyObjective).score = score
    }

    override fun onScoreChanged(score: Score) {
        super.onScoreChanged(score)

        if (dummyObjective == score.objective) {
            players.sendPacket(
                ClientboundSetScorePacket(
                    ServerScoreboard.Method.CHANGE, score.objective!!.name, score.owner, score.score
                )
            )
        }
    }

    override fun addPlayerToTeam(playerName: String, team: PlayerTeam): Boolean {
        return if (super.addPlayerToTeam(playerName, team)) {
            players.sendPacket(ClientboundSetPlayerTeamPacket.createPlayerPacket(team, playerName, ClientboundSetPlayerTeamPacket.Action.ADD))
            true
        } else false
    }

    override fun removePlayerFromTeam(playerName: String, team: PlayerTeam) {
        super.removePlayerFromTeam(playerName, team)
        players.sendPacket(ClientboundSetPlayerTeamPacket.createPlayerPacket(team, playerName, ClientboundSetPlayerTeamPacket.Action.REMOVE))
    }

    override fun onTeamAdded(team: PlayerTeam) {
        super.onTeamAdded(team)
        players.sendPacket(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true))
    }

    override fun onTeamChanged(team: PlayerTeam) {
        super.onTeamChanged(team)
        players.sendPacket(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false))
    }
}
