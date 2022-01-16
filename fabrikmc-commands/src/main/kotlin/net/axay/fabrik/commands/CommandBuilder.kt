package net.axay.fabrik.commands

import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.launch
import net.axay.fabrik.commands.DslAnnotations.NodeLevel.RunsDsl
import net.axay.fabrik.commands.DslAnnotations.NodeLevel.SuggestsDsl
import net.axay.fabrik.commands.DslAnnotations.TopLevel.NodeDsl
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

private class DslAnnotations {
    class TopLevel {
        @Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.CLASS)
        @DslMarker
        annotation class NodeDsl
    }

    class NodeLevel {
        @DslMarker
        annotation class RunsDsl

        @DslMarker
        annotation class SuggestsDsl
    }
}

/**
 * An argument resolver extracts the argument value out of the current [CommandContext].
 */
typealias ArgumentResolver<S, T> = CommandContext<S>.() -> T

/**
 * The simple argument builder is a variant of an [ArgumentCommandBuilder] lambda function
 * that supports [ArgumentResolver] (passed as `it`).
 */
typealias SimpleArgumentBuilder<Source, T> = ArgumentCommandBuilder<Source, T>.(argument: ArgumentResolver<Source, T>) -> Unit

@NodeDsl
abstract class CommandBuilder<Source : CommandSource, Builder : ArgumentBuilder<Source, Builder>> {

    @Suppress("LeakingThis")
    @PublishedApi
    internal val builder = createBrigadierBuilder()

    @PublishedApi
    internal val children = ArrayList<CommandBuilder<Source, *>>()

    private val onToBrigadierBuilders = ArrayList<Builder.() -> Unit>()

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
    @RunsDsl
    inline infix fun runs(crossinline block: CommandContext<Source>.() -> Unit): CommandBuilder<Source, Builder> {
        val previousCommand = builder.command
        builder.executes {
            previousCommand?.run(it)
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
    @RunsDsl
    inline infix fun runsAsync(crossinline block: suspend CommandContext<Source>.() -> Unit) =
        runs {
            fabrikCoroutineScope.launch {
                block(this@runs)
            }
        }

    /**
     * Adds custom execution logic to this command. **DEPRECATED** Use [runs] instead.
     */
    @Deprecated(
        "The name 'simpleExecutes' has been superseded by 'runs'.",
        ReplaceWith("runs { executor.invoke() }")
    )
    inline infix fun simpleExecutes(
        crossinline executor: CommandContext<Source>.() -> Unit,
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
    @NodeDsl
    inline fun literal(name: String, builder: LiteralCommandBuilder<Source>.() -> Unit = {}) =
        LiteralCommandBuilder<Source>(name).apply(builder).also { children += it }

    /**
     * Adds a new argument to this command. This variant of the argument function allows you to specify
     * the [ArgumentType] in the classical Brigadier way.
     *
     * @param name the name of the argument - This will be displayed to the player, if there is enough room for the
     * tooltip.
     * @param type the type of the argument - There are predefined types like `StringArgumentType.string()` or
     * `IdentifierArgumentType.identifier()`. You can also pass a lambda, as [ArgumentType] is a functional
     * interface. For simple types, consider using the `inline reified` version of this function instead.
     */
    @NodeDsl
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
    @NodeDsl
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
    @NodeDsl
    inline fun <reified T> argument(name: String, builder: SimpleArgumentBuilder<Source, T> = {}) =
        ArgumentCommandBuilder<Source, T>(name, ArgumentTypeUtils.fromReifiedType())
            .apply { builder { getArgument(name, T::class.java) } }
            .also { children += it }

    /**
     * Specifies that the given predicate must return true for the [Source]
     * in order for it to be able to execute this part of the command tree. Use
     * this function on the root command node to secure the whole command.
     */
    @RunsDsl
    fun requires(predicate: (source: Source) -> Boolean): CommandBuilder<Source, Builder> {
        builder.requires(builder.requirement.and(predicate))
        return this
    }

    /**
     * Specifies that the given permission [level] is required to execute this part of the command tree.
     * A shortcut delegating to [requires].
     */
    @RunsDsl
    fun requiresPermissionLevel(level: Int) =
        requires { it.hasPermissionLevel(level) }

    /**
     * Specifies that the [PermissionLevel] given as [level] is required to execute this part of the command tree.
     * A shortcut delegating to [requires].
     */
    @RunsDsl
    fun requiresPermissionLevel(level: PermissionLevel) =
        requires { it.hasPermissionLevel(level.level) }

    /**
     * This function allows you to access the regular Brigadier builder. The type of
     * `this` in its context will equal the type of [Builder].
     */
    fun brigadier(block: (@NodeDsl Builder).() -> Unit): CommandBuilder<Source, Builder> {
        onToBrigadierBuilders += block
        return this
    }

    @Deprecated(
        "This function name has a spelling mistake in it, use the brigadier function instead.",
        ReplaceWith("brigadier { block() }")
    )
    fun brigardier(block: (@NodeDsl Builder).() -> Unit) = brigadier(block)

    protected abstract fun createBrigadierBuilder(): Builder

    /**
     * Converts this Kotlin command builder abstraction to an [ArgumentBuilder] of Brigadier.
     * Note that even though this function is public, you probably won't need it in most cases.
     */
    fun toBrigadier(): Builder = createBrigadierBuilder().also { builder ->
        onToBrigadierBuilders.forEach { it(builder) }

        children.forEach {
            @Suppress("UNCHECKED_CAST")
            builder.then(it.toBrigadier() as ArgumentBuilder<Source, *>)
        }
    }
}

class LiteralCommandBuilder<Source : CommandSource>(
    private val name: String,
) : CommandBuilder<Source, LiteralArgumentBuilder<Source>>() {

    override fun createBrigadierBuilder(): LiteralArgumentBuilder<Source> =
        LiteralArgumentBuilder.literal(name)
}

class ArgumentCommandBuilder<Source : CommandSource, T>(
    private val name: String,
    private val type: ArgumentType<T>,
) : CommandBuilder<Source, RequiredArgumentBuilder<Source, T>>() {

    override fun createBrigadierBuilder(): RequiredArgumentBuilder<Source, T> =
        RequiredArgumentBuilder.argument(name, type)

    @PublishedApi
    internal inline fun suggests(
        crossinline block: (context: CommandContext<Source>, builder: SuggestionsBuilder) -> CompletableFuture<Suggestions>,
    ): ArgumentCommandBuilder<Source, T> {
        builder.suggests { context, builder ->
            block(context, builder)
        }
        return this
    }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     */
    @SuggestsDsl
    inline fun suggestSingle(crossinline suggestionBuilder: (CommandContext<Source>) -> Any?) =
        suggests { context, builder ->
            builder.applyAny(suggestionBuilder(context))
            builder.buildFuture()
        }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     * Additionaly, a separate tooltip associated with the suggestion
     * will be shown as well.
     */
    @SuggestsDsl
    inline fun suggestSingleWithTooltip(crossinline suggestionBuilder: (CommandContext<Source>) -> Pair<Any, Message>?) =
        suggests { context, builder ->
            builder.applyAnyWithTooltip(suggestionBuilder(context))
            builder.buildFuture()
        }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     *
     * @param coroutineScope the [CoroutineScope] where the suggestion should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    @SuggestsDsl
    inline fun suggestSingleSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionBuilder: suspend (CommandContext<Source>) -> Any?,
    ) = suggests { context, builder ->
        coroutineScope.async {
            builder.applyAny(suggestionBuilder(context))
            builder.build()
        }.asCompletableFuture()
    }

    /**
     * Suggest the value which is the result of the [suggestionBuilder].
     * Additionaly, a separate tooltip associated with the suggestion
     * will be shown as well.
     *
     * @param coroutineScope the [CoroutineScope] where the suggestion should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    @SuggestsDsl
    inline fun suggestSingleWithTooltipSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionBuilder: suspend (CommandContext<Source>) -> Pair<Any?, Message>?,
    ) = suggests { context, builder ->
        coroutineScope.async {
            builder.applyAnyWithTooltip(suggestionBuilder(context))
            builder.build()
        }.asCompletableFuture()
    }

    /**
     * Suggest the entries of the iterable which is the result of the
     * [suggestionsBuilder].
     */
    @SuggestsDsl
    inline fun suggestList(crossinline suggestionsBuilder: (CommandContext<Source>) -> Iterable<Any?>?) =
        suggests { context, builder ->
            builder.applyIterable(suggestionsBuilder(context))
            builder.buildFuture()
        }

    /**
     * Suggest the entries of the iterable which is the result of the
     * [suggestionsBuilder].
     * Additionaly, a separate tooltip associated with each suggestion
     * will be shown as well.
     */
    @SuggestsDsl
    inline fun suggestListWithTooltips(crossinline suggestionsBuilder: (CommandContext<Source>) -> Iterable<Pair<Any?, Message>?>?) =
        suggests { context, builder ->
            builder.applyIterableWithTooltips(suggestionsBuilder(context))
            builder.buildFuture()
        }

    /**
     * Suggest the entries of the iterable which is the result of the
     * [suggestionsBuilder].
     *
     * @param coroutineScope the [CoroutineScope] where the suggestions should be built in - an async scope by default,
     * but you can change this to a synchronous scope using [net.axay.fabrik.core.task.mcCoroutineScope]
     */
    @SuggestsDsl
    inline fun suggestListSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionsBuilder: suspend (CommandContext<Source>) -> Iterable<Any?>?,
    ) = suggests { context, builder ->
        coroutineScope.async {
            builder.applyIterable(suggestionsBuilder(context))
            builder.build()
        }.asCompletableFuture()
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
    @SuggestsDsl
    inline fun suggestListWithTooltipsSuspending(
        coroutineScope: CoroutineScope = fabrikCoroutineScope,
        crossinline suggestionsBuilder: (CommandContext<Source>) -> Iterable<Pair<Any?, Message>?>?,
    ) = suggests { context, builder ->
        coroutineScope.async {
            builder.applyIterableWithTooltips(suggestionsBuilder(context))
            builder.build()
        }.asCompletableFuture()
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
        suggestionBuilder: suspend (CommandContext<Source>) -> Iterable<Any?>?,
    ) = suggestListSuspending(coroutineScope, suggestionBuilder)
}

/**
 * Creates a new command. Opens a [LiteralCommandBuilder].
 *
 * @param name the name of the root command
 * @param register if true, the command will automatically be registered
 */
inline fun command(
    name: String,
    register: Boolean = true,
    builder: LiteralCommandBuilder<ServerCommandSource>.() -> Unit = {},
): LiteralArgumentBuilder<ServerCommandSource> =
    LiteralCommandBuilder<ServerCommandSource>(name).apply(builder).toBrigadier().apply {
        if (register)
            setupRegistrationCallback()
    }

/**
 * Creates a new client command. Opens a [LiteralCommandBuilder].
 * This command will work on the client, even if the player
 * is connected to a third party server.
 *
 * @param name the name of the root command
 * @param register if true, the command will automatically be registered
 */
@Environment(EnvType.CLIENT)
inline fun clientCommand(
    name: String,
    register: Boolean = true,
    builder: LiteralCommandBuilder<FabricClientCommandSource>.() -> Unit = {},
): LiteralArgumentBuilder<FabricClientCommandSource> =
    LiteralCommandBuilder<FabricClientCommandSource>(name).apply(builder).toBrigadier().apply {
        if (register) register()
    }
