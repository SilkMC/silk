package net.axay.fabrik.commands

import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import net.axay.fabrik.core.task.fabrikCoroutineScope

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
