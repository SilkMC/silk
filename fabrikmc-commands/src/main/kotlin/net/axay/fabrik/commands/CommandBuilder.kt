package net.axay.fabrik.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.launch
import net.axay.fabrik.commands.internal.ArgumentTypeUtils
import net.axay.fabrik.commands.registration.register
import net.axay.fabrik.commands.registration.setupRegistrationCallback
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource
import net.minecraft.command.CommandSource
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

/**
 * An argument resolver extracts the argument value out of the current [CommandContext].
 */
typealias ArgumentResolver<S, T> = CommandContext<S>.() -> T

/**
 * The simple argument builder is a variant of an [ArgumentCommandBuilder] lambda function
 * that supports [ArgumentResolver] (passed as `it`).
 */
typealias SimpleArgumentBuilder<Source, T> = ArgumentCommandBuilder<Source, T>.(argument: ArgumentResolver<Source, T>) -> Unit

abstract class CommandBuilder<Source : CommandSource, Builder : ArgumentBuilder<Source, Builder>> {
    @PublishedApi
    internal val children = ArrayList<CommandBuilder<Source, *>>()

    @PublishedApi
    internal var command: Command<Source>? = null

    /**
     * Adds execution logic to this command. The place where this function
     * is called matters, as this defines for which path in the command tree
     * this executor should be called.
     *
     * possible usage:
     * ```kt
     * command("mycommand") {
     *     // defining runs in the body:
     *     runs { }
     *
     *     // calling runs as an infix function directly after literal or argument:
     *     literal("subcommand") runs { }
     * }
     * ```
     *
     * Note that this function will always return 1 as the exit code.
     *
     * @see com.mojang.brigadier.builder.ArgumentBuilder.executes
     */
    inline infix fun runs(crossinline block: CommandContext<Source>.() -> Unit): CommandBuilder<Source, Builder> {
        command = Command {
            block(it)
            1
        }
        return this
    }

    /**
     * Does the same as [runs] (see its docs for more information), but launches the command
     * logic in an async coroutine.
     *
     * @see runs
     */
    inline infix fun runsAsync(crossinline block: suspend CommandContext<Source>.() -> Unit): CommandBuilder<Source, Builder> {
        command = Command {
            fabrikCoroutineScope.launch {
                block(it)
            }
            1
        }
        return this
    }

    /**
     * Adds custom execution logic to this command. **DEPRECATED** Use [runs] instead.
     */
    @Deprecated(
        "The name 'simpleExecutes' has been superseded by 'runs'.",
        ReplaceWith("runs { executor.invoke() }")
    )
    inline infix fun simpleExecutes(
        crossinline executor: CommandContext<Source>.() -> Unit
    ) = runs(executor)

    /**
     * Adds a new subcommand / literal to this command.
     *
     * possible usage:
     * ```kt
     * command("mycommand") {
     *     literal("subcommand") {
     *         // the body of the subcommand
     *     }
     * }
     * ```
     *
     * @param name the name of the subcommand
     */
    inline fun literal(name: String, builder: LiteralCommandBuilder<Source>.() -> Unit = {}) =
        LiteralCommandBuilder<Source>(name).apply(builder).also { children += it }

    /**
     * Adds a new argument to this command. This variant of the argument function allows you to specify
     * the [ArgumentType] in the classical Brigardier way.
     *
     * @param name the name of the argument - This will be displayed to the player, if there is enough room for the
     * tooltip.
     * @param type the type of the argument - There are predefined types like `StringArgumentType.string()` or
     * `IdentifierArgumentType.identifier()`. You can also pass lambda, as [ArgumentType] is a functional
     * interface. For simple types, consider using the `inline reified` version of this function instead.
     */
    inline fun <reified T> argument(name: String, type: ArgumentType<T>, builder: SimpleArgumentBuilder<Source, T> = {}) =
        ArgumentCommandBuilder<Source, T>(name, type)
            .apply { builder { getArgument(name, T::class.java) } }
            .also { children += it }

    /**
     * Adds a new argument to this command. This variant of the argument function you to specifiy the
     * argument parse logic using a Kotlin lambda function ([parser]).
     *
     * @param name the name of the argument - This will be displayed to the player, if there is enough room for the
     * tooltip.
     * @param parser gives you a [StringReader], which allows you to parse the input of the user - you should return a
     * value of the given type [T], which will be the argument value
     */
    inline fun <reified T> argument(name: String, crossinline parser: (StringReader) -> T, builder: SimpleArgumentBuilder<Source, T> = {}) =
        ArgumentCommandBuilder<Source, T>(name) { parser(it) }
            .apply { builder { getArgument(name, T::class.java) } }
            .also { children += it }

    /**
     * Adds a new argument to this command. The [ArgumentType] will be resolved using the reified
     * type [T]. For a list of supported types, have a look at [ArgumentTypeUtils.fromReifiedType], as it is
     * the function used by this builder function.
     *
     * @param name the name of the argument - This will be displayed to the player, if there is enough room for the
     * tooltip.
     */
    inline fun <reified T> argument(name: String, builder: SimpleArgumentBuilder<Source, T> = {}) =
        ArgumentCommandBuilder<Source, T>(name, ArgumentTypeUtils.fromReifiedType())
            .apply { builder { getArgument(name, T::class.java) } }
            .also { children += it }

    protected abstract fun createBrigardierBuilder(): Builder

    protected open fun Builder.onToBrigardier() = Unit

    /**
     * Converts this Kotlin command builder abstraction to an [ArgumentBuilder] of Brigardier.
     * Note that even though this function is public, you probably won't need it in most cases.
     */
    fun toBrigardier(): Builder = createBrigardierBuilder().also { builder ->
        command?.let { builder.executes(it) }
        builder.onToBrigardier()

        children.forEach {
            @Suppress("UNCHECKED_CAST")
            builder.then(it.toBrigardier() as ArgumentBuilder<Source, *>)
        }
    }
}

class LiteralCommandBuilder<Source : CommandSource>(
    private val name: String,
) : CommandBuilder<Source, LiteralArgumentBuilder<Source>>() {
    override fun createBrigardierBuilder(): LiteralArgumentBuilder<Source> =
        LiteralArgumentBuilder.literal(name)
}

class ArgumentCommandBuilder<Source : CommandSource, T>(
    private val name: String,
    private val type: ArgumentType<T>,
) : CommandBuilder<Source, RequiredArgumentBuilder<Source, T>>() {
    @PublishedApi
    internal var suggestionProvider: SuggestionProvider<Source>? = null

    override fun createBrigardierBuilder(): RequiredArgumentBuilder<Source, T> =
        RequiredArgumentBuilder.argument(name, type)

    override fun RequiredArgumentBuilder<Source, T>.onToBrigardier() {
        suggestionProvider?.let { suggests(it) }
    }

    @PublishedApi
    internal inline fun suggests(crossinline block: (context: CommandContext<Source>, builder: SuggestionsBuilder) -> CompletableFuture<Suggestions>) {
        suggestionProvider = SuggestionProvider { context, builder -> block(context, builder) }
    }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     */
    inline fun suggestSingle(crossinline suggestionBuilder: (CommandContext<Source>) -> Any?) {
        suggests { context, builder ->
            builder.applyAny(suggestionBuilder(context))
            builder.buildFuture()
        }
    }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     * Additionaly, a separate tooltip associated with the suggestion
     * will be shown as well.
     */
    inline fun suggestSingleWithTooltip(crossinline suggestionBuilder: (CommandContext<Source>) -> Pair<Any, Message>?) {
        suggests { context, builder ->
            builder.applyAnyWithTooltip(suggestionBuilder(context))
            builder.buildFuture()
        }
    }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     *
     * @param coroutineScope the [CoroutineScope] where the suggestion should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    inline fun suggestSingleSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionBuilder: suspend (CommandContext<Source>) -> Any?
    ) {
        suggests { context, builder ->
            coroutineScope.async {
                builder.applyAny(suggestionBuilder(context))
                builder.build()
            }.asCompletableFuture()
        }
    }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     * Additionaly, a separate tooltip associated with the suggestion
     * will be shown as well.
     *
     * @param coroutineScope the [CoroutineScope] where the suggestion should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    inline fun suggestSingleWithTooltipSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionBuilder: suspend (CommandContext<Source>) -> Pair<Any?, Message>?
    ) {
        suggests { context, builder ->
            coroutineScope.async {
                builder.applyAnyWithTooltip(suggestionBuilder(context))
                builder.build()
            }.asCompletableFuture()
        }
    }

    /**
     * Suggest the entries of the iterable which is the result of the
     * [suggestionsBuilder].
     */
    inline fun suggestList(crossinline suggestionsBuilder: (CommandContext<Source>) -> Iterable<Any?>?) {
        suggests { context, builder ->
            builder.applyIterable(suggestionsBuilder(context))
            builder.buildFuture()
        }
    }

    /**
     * Suggest the entries of the iterable which is the result of the
     * [suggestionsBuilder].
     * Additionaly, a separate tooltip associated with each suggestion
     * will be shown as well.
     */
    inline fun suggestListWithTooltips(crossinline suggestionsBuilder: (CommandContext<Source>) -> Iterable<Pair<Any?, Message>?>?) {
        suggests { context, builder ->
            builder.applyIterableWithTooltips(suggestionsBuilder(context))
            builder.buildFuture()
        }
    }

    /**
     * Suggest the entries of the iterable which is the result of the
     * [suggestionsBuilder].
     *
     * @param coroutineScope the [CoroutineScope] where the suggestions should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    inline fun suggestListSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionsBuilder: suspend (CommandContext<Source>) -> Iterable<Any?>?
    ) {
        suggests { context, builder ->
            coroutineScope.async {
                builder.applyIterable(suggestionsBuilder(context))
                builder.build()
            }.asCompletableFuture()
        }
    }

    /**
     * Suggest the entries of the iterable which is the result of the
     * [suggestionsBuilder].
     * Additionaly, a separate tooltip associated with each suggestion
     * will be shown as well.
     *
     * @param coroutineScope the [CoroutineScope] where the suggestions should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    inline fun suggestListWithTooltipsSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionsBuilder: (CommandContext<Source>) -> Iterable<Pair<Any?, Message>?>?
    ) {
        suggests { context, builder ->
            coroutineScope.async {
                builder.applyIterableWithTooltips(suggestionsBuilder(context))
                builder.build()
            }.asCompletableFuture()
        }
    }

    @PublishedApi
    internal fun SuggestionsBuilder.applyAny(any: Any?) {
        when (any) {
            is Int -> suggest(any)
            is String -> suggest(any)
            else -> suggest(any.toString())
        }
    }


    @PublishedApi
    internal fun SuggestionsBuilder.applyAnyWithTooltip(pair: Pair<Any?, Message>?) {
        if (pair == null) return
        val (any, message) = pair
        when (any) {
            is Int -> suggest(any, message)
            is String -> suggest(any, message)
            else -> suggest(any.toString(), message)
        }
    }

    @PublishedApi
    internal fun SuggestionsBuilder.applyIterable(iterable: Iterable<Any?>?) =
        iterable?.forEach { applyAny(it) }

    @PublishedApi
    internal fun SuggestionsBuilder.applyIterableWithTooltips(iterable: Iterable<Pair<Any?, Message>?>?) =
        iterable?.forEach { applyAnyWithTooltip(it) }

    /**
     * Adds custom suspending suggestion logic for an argument.
     *
     * @param coroutineScope the [CoroutineScope] where the suggestions should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    @Deprecated(
        "The name 'simpleSuggests' has been superseded by 'suggestListSuspending'",
        ReplaceWith("suggestListSuspending(coroutineScope, suggestionBuilder)")
    )
    fun simpleSuggests(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        suggestionBuilder: suspend (CommandContext<Source>) -> Iterable<Any?>?
    ) = suggestListSuspending(coroutineScope, suggestionBuilder)
}

/**
 * Creates a new command.
 *
 * @param name the name of the root command
 * @param register if true, the command will automatically be registered
 */
inline fun command(
    name: String,
    register: Boolean = true,
    builder: LiteralCommandBuilder<ServerCommandSource>.() -> Unit = {},
): LiteralArgumentBuilder<ServerCommandSource> =
    LiteralCommandBuilder<ServerCommandSource>(name).apply(builder).toBrigardier().apply {
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
    builder: LiteralCommandBuilder<FabricClientCommandSource>.() -> Unit = {},
): LiteralArgumentBuilder<FabricClientCommandSource> =
    LiteralCommandBuilder<FabricClientCommandSource>(name).apply(builder).toBrigardier().apply {
        if (register) register()
    }
