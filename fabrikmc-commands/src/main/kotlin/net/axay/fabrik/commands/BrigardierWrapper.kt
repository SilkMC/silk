package net.axay.fabrik.commands

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import net.axay.fabrik.core.task.fabrikCoroutineScope
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
 * Add custom execution logic for this command.
 */
inline fun <S> ArgumentBuilder<S, *>.simpleExecutes(
    crossinline executor: SimpleCommandContext<S>.() -> Unit
) {
    executes wrapped@{
        executor.invoke(SimpleCommandContext(it))
        return@wrapped 1
    }
}

/**
 * Adds custom suspending suggestion logic for an argument.
 *
 * @param coroutineScope the [CoroutineScope] where the suggestions should be built in - an async scope by default,
 * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
 */
fun <S> RequiredArgumentBuilder<S, *>.simpleSuggests(
    coroutineScope: CoroutineScope = fabrikCoroutineScope,
    suggestionBuilder: suspend (CommandContext<S>) -> Iterable<Any?>?
) {
    suggests { context, builder ->
        coroutineScope.async {
            suggestionBuilder.invoke(context)?.forEach {
                if (it is Int)
                    builder.suggest(it)
                else
                    builder.suggest(it.toString())
            }
            builder.build()
        }.asCompletableFuture()
    }
}

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
