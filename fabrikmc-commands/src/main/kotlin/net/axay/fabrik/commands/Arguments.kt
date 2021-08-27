package net.axay.fabrik.commands

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.axay.fabrik.commands.internal.ArgumentTypeUtils
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

/**
 * Adds an argument.
 *
 * @param name the name of the argument
 * @param type the type of the argument - e.g. IntegerArgumentType.integer() or StringArgumentType.string()
 */
inline fun <T> ArgumentBuilder<ServerCommandSource, *>.argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<ServerCommandSource, T>.() -> Unit = {}
): RequiredArgumentBuilder<ServerCommandSource, T> =
    CommandManager.argument(name, type).apply(builder).also { then(it) }

/**
 * Adds an argument. The argument type will be resolved via the reified
 * type [T].
 *
 * @param name the name of the argument
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> ArgumentBuilder<ServerCommandSource, *>.argument(
    name: String,
    builder: RequiredArgumentBuilder<ServerCommandSource, T>.() -> Unit = {}
): RequiredArgumentBuilder<ServerCommandSource, T> =
    CommandManager.argument(name, ArgumentTypeUtils.fromReifiedType<T>()).apply(builder).also { then(it) }

/**
 * Adds an argument.
 *
 * @param name the name of the argument
 * @param type the type of the argument - e.g. IntegerArgumentType.integer() or StringArgumentType.string()
 */
@Environment(EnvType.CLIENT)
@JvmName("clientArgument")
inline fun <T> ArgumentBuilder<FabricClientCommandSource, *>.argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<FabricClientCommandSource, T>.() -> Unit = {}
): RequiredArgumentBuilder<FabricClientCommandSource, T> =
    ClientCommandManager.argument(name, type).apply(builder).also { then(it) }

/**
 * Adds an argument. The argument type will be resolved via the reified
 * type [T].
 *
 * @param name the name of the argument
 */
@Environment(EnvType.CLIENT)
@JvmName("clientArgument")
inline fun <reified T> ArgumentBuilder<FabricClientCommandSource, *>.argument(
    name: String,
    builder: RequiredArgumentBuilder<FabricClientCommandSource, T>.() -> Unit = {}
): RequiredArgumentBuilder<FabricClientCommandSource, T> =
    ClientCommandManager.argument(name, ArgumentTypeUtils.fromReifiedType<T>()).apply(builder).also { then(it) }

/**
 * Get the value of this argument.
 */
inline fun <reified T> CommandContext<ServerCommandSource>.getArgument(name: String): T =
    getArgument(name, T::class.java)

/**
 * Get the value of this argument.
 */
@Environment(EnvType.CLIENT)
@JvmName("clientGetArgument")
inline fun <reified T> CommandContext<FabricClientCommandSource>.getArgument(name: String): T =
    getArgument(name, T::class.java)
