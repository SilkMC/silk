package net.silkmc.silk.game.tablist

import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket
import net.minecraft.network.protocol.game.ClientboundTabListPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.packet.sendPacket
import net.silkmc.silk.core.task.initWithServerAsync
import net.silkmc.silk.core.task.silkCoroutineScope
import net.silkmc.silk.core.text.literal
import net.silkmc.silk.game.scoreboard.ScoreboardLine
import java.util.*


/**
 * A tablist configuration which will be displayed to all players.
 * You can reload the [playerNames] using the [updateNames]/[updateName] function.
 *
 * **Note:** You probably want to build this class using the tablist builder API. See [tablist]!
 */
class Tablist(
    private val nameGenerator: (suspend ServerPlayer.() -> Pair<Component, String>)?,
    private val headers: List<ScoreboardLine>,
    private val footers: List<ScoreboardLine>
) {

    companion object {
        @JvmStatic
        var currentTablist: Tablist? = null
    }

    @InternalSilkApi
    val currentHeaders: MutableList<Component> = mutableListOf()

    @InternalSilkApi
    val currentFooters: MutableList<Component> = mutableListOf()

    @InternalSilkApi
    val headerFooterDeferred = initWithServerAsync {
        silkCoroutineScope.launch {
            currentFooters.preFill(footers)
            currentHeaders.preFill(headers)
        }

        Silk.currentServer?.sendTablistUpdate(currentHeaders, currentFooters)

        headers.forEachIndexed { index, scoreboardLine ->
            silkCoroutineScope.launch {
                scoreboardLine.textFlow.collect {
                    if (currentHeaders.getOrNull(index) == null) currentHeaders += it
                    else currentHeaders[index] = it
                    Silk.currentServer?.sendTablistUpdate(currentHeaders, currentFooters)
                }
            }
        }
        footers.forEachIndexed { index, scoreboardLine ->
            silkCoroutineScope.launch {
                scoreboardLine.textFlow.collect {
                    if (currentFooters.getOrNull(index) == null) currentFooters += it
                    else currentFooters[index] = it
                    Silk.currentServer?.sendTablistUpdate(currentHeaders, currentFooters)
                }
            }
        }
    }

    @InternalSilkApi
    val playerNames = HashMap<UUID, Pair<Component, String>>()

    /**
     * Regenerates all player names and priorities
     */
    fun updateNames() {
        if (nameGenerator == null) return
        val server = Silk.currentServer
        if (server?.isRunning == false) return

        silkCoroutineScope.launch {
            server?.playerList?.players?.forEach {
                val nameGen = nameGenerator.invoke(it)
                playerNames[it.uuid] = nameGen
            }

            playerNames.forEach { (uuid, pair) ->
                val player = server?.playerList?.getPlayer(uuid) ?: return@forEach
                val team = player.scoreboard.getPlayerTeam(pair.second) ?: player.scoreboard.addPlayerTeam(
                    pair.second
                )
                player.scoreboard.addPlayerToTeam(player.scoreboardName, team)
            }

            server?.playerList?.players?.sendPacket(
                ClientboundPlayerInfoPacket(
                    ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, server.playerList.players
                )
            )
        }
    }

    /**
     * Regenerate the name and priority of the [player]
     *
     * @param player the player whose attributes should be updated
     */
    fun updateName(player: ServerPlayer) {
        if (nameGenerator == null) return
        if (Silk.currentServer?.isRunning == false) return
        silkCoroutineScope.launch {
            val nameGen = nameGenerator.invoke(player)
            playerNames[player.uuid] = nameGen
            val team = player.scoreboard.getPlayerTeam(nameGen.second) ?: player.scoreboard.addPlayerTeam(
                nameGen.second
            )
            player.scoreboard.addPlayerToTeam(player.scoreboardName, team)

            player.server.playerList.players.sendPacket(
                ClientboundPlayerInfoPacket(
                    ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, player
                )
            )
        }
    }

    /**
     * Generates the [player]'s name and adds it to the [playerNames] list
     *
     * @param player the player whose name should be added
     */
    @InternalSilkApi
    fun addPlayer(player: ServerPlayer) {
        if (nameGenerator == null) return
        silkCoroutineScope.launch {
            val nameGen = nameGenerator.invoke(player)
            playerNames[player.uuid] = nameGen
            headerFooterDeferred.await()
            val team = player.scoreboard.getPlayerTeam(nameGen.second) ?: player.scoreboard.addPlayerTeam(
                nameGen.second
            )
            player.scoreboard.addPlayerToTeam(player.scoreboardName, team)
            player.server.playerList.players.sendPacket(
                ClientboundPlayerInfoPacket(
                    ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, player
                )
            )
        }
    }

    /**
     * Removes the [player]'s name from the [playerNames] list
     *
     * @param player the player whose name should be removed
     */
    @InternalSilkApi
    fun removePlayer(player: ServerPlayer) {
        playerNames.remove(player.uuid)
    }

    init {
        currentTablist = this
    }
}

private fun List<Component>.merge(): Component {
    return Component.empty().also {
        this.forEachIndexed { i, component ->
            it.append(component)
            if (this.size - 1 > i) it.append("\n".literal)
        }
    }
}

private fun MinecraftServer?.sendTablistUpdate(headers: List<Component>, footers: List<Component>) {
    this?.playerList?.players?.forEach {
        it.connection.send(
            ClientboundTabListPacket(
                headers.merge(), footers.merge()
            )
        )
    }
}

private suspend fun MutableList<Component>.preFill(source: List<ScoreboardLine>) {
    if (this.size >= source.size) return
    repeat(source.size - this.size) { this += Component.empty() }
    source.filterIsInstance<ScoreboardLine.Updatable>().forEachIndexed { index, scoreboardLine ->
        this[index] = scoreboardLine.initial ?: Component.empty()
    }
    source.filterIsInstance<ScoreboardLine.Static>().forEachIndexed { index, scoreboardLine ->
        this[index] = scoreboardLine.textFlow.last()
    }
}