package net.silkmc.silk.game.sideboard.internal

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.numbers.NumberFormat
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundResetScorePacket
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetScorePacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.Objective
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Player
import net.silkmc.silk.core.kotlin.LimitedAccessWrapper
import net.silkmc.silk.core.task.silkCoroutineScope
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
    val numberFormat: NumberFormat
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

    private val dummyObjective = Objective(NoopScoreboard, name, ObjectiveCriteria.DUMMY, displayName, ObjectiveCriteria.RenderType.INTEGER, false, numberFormat)

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
                    it.reversed().forEachIndexed { _, line ->
                        this@buildList.add(line.createContentPacket())
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

    inner class Line(number: Int, private var content: Component) {
        private val fakePlayerName = "line$number"
        private val score = -number

        fun createContentPacket(): Packet<*> {
            return ClientboundSetScorePacket(fakePlayerName, dummyObjective.name, score, Optional.of(content), Optional.of(numberFormat))
        }

        fun setContent(content: Component) {
            this.content = content
            emitPacket(createContentPacket())
        }

        fun createRemovePackets(): List<Packet<*>> {
            return buildList {
                add(ClientboundResetScorePacket(fakePlayerName, dummyObjective.name))
            }
        }
    }

    suspend fun addLine(initialContent: Component): Line {
        val line: Line
        lines.access {
            val index = it.lastIndex + 1
            line = Line(index, initialContent)
            it.add(line)
        }
        return line
    }
}
