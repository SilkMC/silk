package net.silkmc.silk.core.server

import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import java.nio.file.Path
import kotlin.io.path.absolute

/**
 * Executes the given [command] for this server.
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun MinecraftServer.executeCommand(command: String) {
    executeCommand(command, this.createCommandSourceStack())
}

/**
 * Executes the given [command] for the specified [source].
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun MinecraftServer.executeCommand(command: String, source: CommandSourceStack) {
    val parsed = commands.dispatcher.parse(command, source)
    commands.performCommand(parsed, command)
}

/**
 * Returns the list of current online players, or an empty list if no
 * players are online.
 */
val MinecraftServer.players: List<ServerPlayer>
    get() = playerList.players

/**
 * Returns the current run directory of the server as an
 * absolute [Path].
 */
@Deprecated(
    message = "Minecraft now offers a 'serverDirectory' property, use that instead.",
    replaceWith = ReplaceWith("serverDirectory.absolute()", "kotlin.io.path.absolute"),
)
val MinecraftServer.serverPath: Path
    get() = serverDirectory.absolute()
