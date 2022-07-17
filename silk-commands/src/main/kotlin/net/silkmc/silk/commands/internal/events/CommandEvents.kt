package net.silkmc.silk.commands.internal.events

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.CommandSelection
import net.silkmc.silk.commands.ClientCommandSourceStack
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.events.Events
import net.silkmc.silk.core.internal.events.Event

/**
 * Events related to commands.
 */
@Suppress("unused") // receiver is for namespacing only
val Events.command get() = CommandEvents

@ExperimentalSilkApi
object CommandEvents {

    class RegisterEvent(
        val dispatcher: CommandDispatcher<CommandSourceStack>,
        val context: CommandBuildContext,
        val selection: CommandSelection
    )

    class RegisterEventClient(
        val dispatcher: CommandDispatcher<ClientCommandSourceStack>,
        val context: CommandBuildContext,
    )

    /**
     * Called when server commands are registered.
     */
    val register = Event.onlySync<RegisterEvent>()

    /**
     * Called when client commands are registered.
     */
    val registerClient = Event.onlySync<RegisterEventClient>()
}
