package net.axay.fabrik.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.axay.fabrik.commands.registration.register
import net.axay.fabrik.commands.registration.setupRegistrationCallback
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

/**
 * Creates a new command.
 *
 * @param name the name of the root command
 * @param register if true, the command will automatically be registered
 */
inline fun command(
    name: String,
    register: Boolean = true,
    builder: LiteralArgumentBuilder<ServerCommandSource>.() -> Unit
): LiteralArgumentBuilder<ServerCommandSource> =
    CommandManager.literal(name).apply(builder).apply {
        if (register)
            setupRegistrationCallback()
    }

/**
 * Creates a new client command.
 * This command will work on the client, even if the player
 * is connected to a third party server.
 *
 * @param name the name of the root command
 * @param register if true, the command will automatically be registered
 */
@Environment(EnvType.CLIENT)
inline fun clientCommand(
    name: String,
    register: Boolean = false,
    builder: LiteralArgumentBuilder<FabricClientCommandSource>.() -> Unit
): LiteralArgumentBuilder<FabricClientCommandSource> =
    ClientCommandManager.literal(name).apply(builder).apply {
        if (register) register()
    }

/**
 * Adds a new literal to this command.
 *
 * @param name the name of the literal
 */
inline fun ArgumentBuilder<ServerCommandSource, *>.literal(
    name: String,
    builder: LiteralArgumentBuilder<ServerCommandSource>.() -> Unit = {}
) = command(name, false, builder).also { then(it) }

/**
 * Adds a new literal to this command.
 *
 * @param name the name of the literal
 */
@Environment(EnvType.CLIENT)
@JvmName("clientLiteral")
inline fun ArgumentBuilder<FabricClientCommandSource, *>.literal(
    name: String,
    builder: LiteralArgumentBuilder<FabricClientCommandSource>.() -> Unit = {}
) = clientCommand(name, false, builder).also { then(it) }
