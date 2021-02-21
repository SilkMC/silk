package net.axay.fabrik.commands

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

private typealias SCS = ServerCommandSource

inline fun command(
    name: String,
    builder: LiteralArgumentBuilder<SCS>.() -> Unit
): LiteralArgumentBuilder<SCS> =
    CommandManager.literal(name).apply(builder)

inline fun LiteralArgumentBuilder<SCS>.simpleExecutes(
    crossinline executor: (CommandContext<SCS>) -> Unit
): LiteralArgumentBuilder<SCS> =
    executes wrapped@{
        executor.invoke(it)
        return@wrapped 1
    }

inline fun LiteralArgumentBuilder<SCS>.literal(
    name: String,
    builder: LiteralArgumentBuilder<SCS>.() -> Unit
): LiteralArgumentBuilder<SCS> =
    then(command(name, builder))

inline fun <T> LiteralArgumentBuilder<SCS>.argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<SCS, T>.() -> Unit
): LiteralArgumentBuilder<SCS> =
    then(CommandManager.argument(name, type).apply(builder))

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
