package net.axay.fabrik.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import net.axay.fabrik.commands.internal.SimpleCommandContext

/**
 * Add custom execution logic for this command.
 */
inline infix fun <S> ArgumentBuilder<S, *>.simpleExecutes(
    crossinline executor: SimpleCommandContext<S>.() -> Unit
) {
    executes wrapped@{
        executor.invoke(SimpleCommandContext(it))
        return@wrapped 1
    }
}
