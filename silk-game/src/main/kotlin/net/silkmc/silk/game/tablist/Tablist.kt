package net.silkmc.silk.game.tablist

import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket
import net.minecraft.network.protocol.game.ClientboundTabListPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.task.silkCoroutineScope
import net.silkmc.silk.core.text.literalText
import net.silkmc.silk.game.scoreboard.ScoreboardLine
import java.util.*


/**
 * A tablist configuration which will be displayed to all players.
 * You can reload the [playerNames] using the [updateNames]/[updateName] function.
 *
 * **Note:** You probably want to build this class using the tablist builder API. See [tablist]!
 */
class Tablist(
    private val nameGenerator: (suspend ServerPlayer.() -> Component)?,
    headers: List<ScoreboardLine>,
    footers: List<ScoreboardLine>
) {

    companion object {
        @JvmStatic
        var currentTablist: Tablist? = null
    }

    @InternalSilkApi
    val headerFooterUpdateTask = infiniteMcCoroutineTask {
        val currentHeaders: ArrayList<Component> = ArrayList()
        val currentFooters: ArrayList<Component> = ArrayList()

        headers.handleUpdating(currentHeaders) {
            Silk.currentServer?.sendTablistUpdate(currentHeaders, currentFooters)
        }

        footers.handleUpdating(currentFooters) {
            Silk.currentServer?.sendTablistUpdate(currentHeaders, currentFooters)
        }
    }

    @InternalSilkApi
    val playerNames = HashMap<UUID, Component>()


    /**
     * Regenerates all player names playing on the server.
     */
    fun updateNames() {
        if (nameGenerator == null) return
        val server = Silk.currentServer
        if (server?.isRunning == false) return
        silkCoroutineScope.launch {
            server?.playerList?.players?.forEach {
                playerNames[it.uuid] = nameGenerator.invoke(it)
            }

            server?.playerList?.players?.forEach {
                it.connection.send(
                    ClientboundPlayerInfoPacket(
                        ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, server.playerList.players
                    )
                )
            }
        }
    }

    /**
     * Regenerate the name of the given [player]
     *
     * @param player the player whose name should be updated
     */
    fun updateName(player: ServerPlayer) {
        if (nameGenerator == null) return
        if (Silk.currentServer?.isRunning == false) return
        silkCoroutineScope.launch {
            playerNames[player.uuid] = nameGenerator.invoke(player)

            Silk.currentServer?.playerList?.players?.forEach {
                it.connection.send(
                    ClientboundPlayerInfoPacket(
                        ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, it
                    )
                )
            }
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
            playerNames[player.uuid] = nameGenerator.invoke(player)
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

    /**
     * Disables the [headerFooterUpdateTask]
     */
    fun disable() {
        headerFooterUpdateTask.cancel()
        currentTablist = null
    }

    /**
     * Enables the [headerFooterUpdateTask]
     */
    fun enable() {
        headerFooterUpdateTask.start()
        currentTablist = this
    }
}

private fun List<Component>.merge(): Component {
    val merged = Component.empty()
    this.forEachIndexed { index, component ->
        merged.append(component)
        if (index >= this.size) merged.append(literalText { newLine() })
    }
    return merged
}

private fun List<ScoreboardLine>.handleUpdating(currentList: ArrayList<Component>, action: suspend () -> Unit) {
    this.forEachIndexed { index, line ->
        silkCoroutineScope.launch {
            line.textFlow.collect { component ->
                currentList[index] = component
                action()
            }
        }
    }
}

private fun MinecraftServer.sendTablistUpdate(headers: List<Component>, footers: List<Component>) {
    this.playerList.players.forEach {
        it.connection.send(
            ClientboundTabListPacket(
                headers.merge(), footers.merge()
            )
        )
    }
}