@file:Suppress("unused")

package net.silkmc.silk.core.server

import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
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
 * Returns the current run directory of the server as an absolute [Path].
 */
@Deprecated(
    message = "Minecraft now offers a 'serverDirectory' property, use that instead.",
    replaceWith = ReplaceWith("serverDirectory.absolute()", "kotlin.io.path.absolute"),
)
val MinecraftServer.serverPath: Path
    get() = serverDirectory.absolute()

/**
 * Retrieves the level for an associated with an id is present
 *
 * @param id The id of the level to check
 * @return The level
 */
fun MinecraftServer.getLevel(id: ResourceLocation): ServerLevel? = getLevel(ResourceKey.create(Registries.DIMENSION, id))

/**
 * Checks if the level associated with an id is present
 *
 * @param id The id of the level to check
 * @return If the level is present
 */
fun MinecraftServer.hasLevel(id: ResourceLocation): Boolean = getLevel(id) != null
