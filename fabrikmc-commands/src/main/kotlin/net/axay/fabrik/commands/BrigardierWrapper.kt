package net.axay.fabrik.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
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
    crossinline command: (CommandContext<SCS>) -> Unit
): LiteralArgumentBuilder<SCS> =
    executes wrapped@{
        command.invoke(it)
        return@wrapped 1
    }

inline fun LiteralArgumentBuilder<SCS>.literal(
    name: String,
    builder: LiteralArgumentBuilder<SCS>.() -> Unit
): LiteralArgumentBuilder<SCS> =
    then(command(name, builder))
