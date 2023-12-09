package net.silkmc.silk.game.sideboard.internal

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.numbers.BlankFormat
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundResetScorePacket
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket
import net.minecraft.network.protocol.game.ClientboundSetScorePacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.Objective
import net.minecraft.world.scores.PlayerTeam
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Player
import net.silkmc.silk.core.kotlin.LimitedAccessWrapper
import net.silkmc.silk.core.task.silkCoroutineScope
import net.silkmc.silk.core.text.literal
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * A server side scoreboard which is only displayed to
 * a selected collection of players.
 * This implementation is used by the [net.silkmc.silk.game.sideboard.Sideboard]
 * api.
 *
 * The packet implementation of this class is not completed, thus
 * it is internal for now.
 *
 * Have a look at [net.minecraft.server.ServerScoreboard] for a
 * complete packet implementation.
 */
@InternalSilkApi
class SideboardScoreboard(
    name: String,
    displayName: Component,
) {

    companion object {
        private val playerBoards = ConcurrentHashMap<UUID, SideboardScoreboard>().also { map ->
            Events.Player.preQuit.listen { event ->
                silkCoroutineScope.launch {
                    map.remove(event.player.uuid)?.hideFromPlayer(event.player, sendRemove = false)
                }
            }
        }
    }

    private val dummyObjective = Objective(NoopScoreboard, name, ObjectiveCriteria.DUMMY, displayName, ObjectiveCriteria.RenderType.INTEGER, false, BlankFormat.INSTANCE)

    // scoreboard state
    private val lines = LimitedAccessWrapper(ArrayList<Line>())

    // for sending state to player
    private val packetFlow = MutableSharedFlow<Packet<*>>()
    private val playerJobs = ConcurrentHashMap<UUID, Job>()

    /**
     * See [net.silkmc.silk.game.sideboard.Sideboard.displayToPlayer].
     */
    suspend fun displayToPlayer(player: ServerPlayer) {
        // already start to create the init packets in the background
        val initPacketsDeferred = silkCoroutineScope.async {
            buildList {
                // send required dummy objective
                add(ClientboundSetObjectivePacket(dummyObjective, ClientboundSetObjectivePacket.METHOD_ADD))

                // add line packets
                lines.access {
                    it.reversed().forEachIndexed { index, line ->
                        this@buildList.addAll(line.createInitPackets(index))
                    }
                }

                // display at the side
                add(ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, dummyObjective))
            }
        }

        // remove the previous board
        playerBoards.put(player.uuid, this)
            ?.hideFromPlayer(player)

        val packetJob = silkCoroutineScope.launch(start = CoroutineStart.LAZY) {
            initPacketsDeferred.await().forEach(player.connection::send)
            packetFlow.collect(player.connection::send)
        }

        playerJobs.put(player.uuid, packetJob)
            // shouldn't be necessary, but leaving this here to really make sure there are no competing jobs
            ?.cancelAndJoin()

        packetJob.start()
    }

    /**
     * See [net.silkmc.silk.game.sideboard.Sideboard.hideFromPlayer].
     */
    suspend fun hideFromPlayer(player: ServerPlayer, sendRemove: Boolean = true) {
        // cancel packet collector job
        playerJobs.remove(player.uuid)?.cancelAndJoin()
        // might already be removed, but make sure it is
        playerBoards.remove(player.uuid, this)

        if (sendRemove) {
            lines.access {
                it.forEach { line ->
                    line.createRemovePackets().forEach(player.connection::send)
                }
            }
            player.connection.send(ClientboundSetObjectivePacket(dummyObjective, ClientboundSetObjectivePacket.METHOD_REMOVE))
        }
    }

    private fun emitPacket(packet: Packet<*>) {
        silkCoroutineScope.launch {
            packetFlow.emit(packet)
        }
    }

    inner class Line(number: Int) {
        private val fakePlayerName = number.toString().map { "§${it}" }.joinToString("")
        private val teamName = "team_${number}"

        private val unsafeTeam = PlayerTeam(NoopScoreboard, teamName)
        private val wrappedTeam = LimitedAccessWrapper(unsafeTeam)

        suspend fun setContent(text: Component) {
            val packet: Packet<*>
            wrappedTeam.access { team ->
                team.playerPrefix = text
                packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, false)
            }
            emitPacket(packet)
        }

        suspend fun createInitPackets(score: Int): List<Packet<*>> {
            return buildList {
                add(ClientboundSetScorePacket(fakePlayerName, dummyObjective.name, score, fakePlayerName.literal, BlankFormat.INSTANCE))
                wrappedTeam.access { team ->
                    add(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true))
                }
                add(ClientboundSetPlayerTeamPacket.createPlayerPacket(unsafeTeam, fakePlayerName, ClientboundSetPlayerTeamPacket.Action.ADD))
            }
        }

        fun createRemovePackets(): List<Packet<*>> {
            return buildList {
                add(ClientboundResetScorePacket(fakePlayerName, dummyObjective.name))
                add(ClientboundSetPlayerTeamPacket.createRemovePacket(unsafeTeam))
            }
        }
    }

    suspend fun addLine(): Line {
        val line: Line
        lines.access {
            val index = it.lastIndex + 1
            line = Line(index)
            it.add(line)
        }
        return line
    }
}
