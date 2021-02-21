package net.axay.fabrik.commands

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

private typealias SCS = ServerCommandSource

/**
 * Create a new command.
 */
inline fun command(
    name: String,
    builder: LiteralArgumentBuilder<SCS>.() -> Unit
): LiteralArgumentBuilder<SCS> =
    CommandManager.literal(name).apply(builder)

/**
 * Add custom execution logic for this command.
 */
inline fun LiteralArgumentBuilder<SCS>.simpleExecutes(
    crossinline executor: (CommandContext<SCS>) -> Unit
): LiteralArgumentBuilder<SCS> =
    executes wrapped@{
        executor.invoke(it)
        return@wrapped 1
    }

/**
 * Add a new literal to this command.
 */
inline fun LiteralArgumentBuilder<SCS>.literal(
    name: String,
    builder: LiteralArgumentBuilder<SCS>.() -> Unit
): LiteralArgumentBuilder<SCS> =
    then(command(name, builder))

/**
 * Add an argument.
 */
inline fun <T> LiteralArgumentBuilder<SCS>.argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<SCS, T>.() -> Unit
): LiteralArgumentBuilder<SCS> =
    then(CommandManager.argument(name, type).apply(builder))

/**
 * Add custom suggestion logic for an argument.
 */
inline fun RequiredArgumentBuilder<SCS, *>.simpleSuggests(
    crossinline suggestionBuilder: CommandContext<SCS>.() -> Iterable<Any?>
) {
    suggests { context, builder ->
        suggestionBuilder.invoke(context).forEach {
            if (it is Int)
                builder.suggest(it)
            else
                builder.suggest(it.toString())
        }
        builder.buildFuture()
    }
}
