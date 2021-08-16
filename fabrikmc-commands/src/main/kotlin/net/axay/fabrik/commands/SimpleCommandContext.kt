package net.axay.fabrik.commands

import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import net.axay.fabrik.commands.mixin.CommandContextAccessor

@Suppress("UNCHECKED_CAST")
class SimpleCommandContext<S>(context: CommandContext<S>) : CommandContext<S>(
    context.source,
    context.input,
    (context as CommandContextAccessor).arguments as MutableMap<String, ParsedArgument<S, *>>,
    context.command,
    context.rootNode,
    context.nodes,
    context.range,
    context.child,
    context.modifier as RedirectModifier<S>,
    context.forks
) {
    /**
     * Resolves the argument specified by this context builder function of the
     * type [RequiredArgumentBuilder].
     *
     * The argument itself will be taken from the current [CommandContext].
     */
    inline fun <reified T> RequiredArgumentBuilder<S, T>.resolveArgument(): T =
        getArgument(name, T::class.java)
}
