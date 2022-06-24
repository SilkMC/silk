package net.silkmc.silk.commands.internal

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.CommandNode
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.player.LocalPlayer
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.ComponentUtils
import net.silkmc.silk.commands.ClientCommandSourceStack
import net.silkmc.silk.commands.internal.events.CommandEvents
import net.silkmc.silk.commands.sendFailure
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.logging.logWarning

@InternalSilkApi
@Environment(EnvType.CLIENT)
object ClientCommandHandler {
    private var currentDispatcher: CommandDispatcher<ClientCommandSourceStack>? = null

    fun refreshDispatcher(context: CommandBuildContext) {
        val dispatcher = CommandDispatcher<ClientCommandSourceStack>()
        currentDispatcher = dispatcher

        CommandEvents.RegisterClient.invoke(CommandEvents.RegisterClient(dispatcher, context))
        dispatcher.findAmbiguities { _, child, sibling, inputs ->
            logWarning("Ambiguity between arguments ${dispatcher.getPath(child)} and ${dispatcher.getPath(sibling)} with inputs: $inputs");
        }
    }

    fun applyCommands(dispatcher: CommandDispatcher<SharedSuggestionProvider>) {
        currentDispatcher?.root?.children?.forEach {
            @Suppress("UNCHECKED_CAST")
            dispatcher.root.addChild(it as CommandNode<SharedSuggestionProvider>)
        }
    }

    fun executeCommand(command: String, player: LocalPlayer): Boolean {
        val dispatcher = currentDispatcher ?: return false
        val source = player.connection.suggestionsProvider

        try {
            dispatcher.execute(command, source)
            return true
        } catch (exc: CommandSyntaxException) {
            if (exc.type == CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand()) {
                return false
            }
            source.sendFailure(ComponentUtils.fromMessage(exc.rawMessage))
            return true
        }
    }
}
