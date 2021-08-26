package net.axay.fabrik.commands.internal

import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

// the following class and all usages of reflection in it are only required until kotlin gets
// the context keyword for extension functions

@Suppress("UNCHECKED_CAST")
class SimpleCommandContext<S>(context: CommandContext<S>) : CommandContext<S>(
    context.source,
    context.input,
    commandContextArgumentsField.get(context) as MutableMap<String, ParsedArgument<S, *>>?,
    context.command,
    context.rootNode,
    context.nodes,
    context.range,
    context.child,
    commandContextModifierField.get(context) as RedirectModifier<S>?,
    commandContextForksField.get(context) as Boolean
) {
    companion object {
        private val commandContextArgumentsField = CommandContext::class.declaredMemberProperties
            .first { it.name == "arguments" }
            .apply { isAccessible = true }

        private val commandContextModifierField = CommandContext::class.declaredMemberProperties
            .first { it.name == "modifier" }
            .apply { isAccessible = true }

        private val commandContextForksField = CommandContext::class.declaredMemberProperties
            .first { it.name == "forks" }
            .apply { isAccessible = true }
    }

    /**
     * Resolves the argument specified by this context builder function of the
     * type [RequiredArgumentBuilder].
     *
     * The argument itself will be taken from the current [CommandContext].
     */
    inline fun <reified T> RequiredArgumentBuilder<S, T>.resolveArgument(): T =
        getArgument(name, T::class.java)
}
