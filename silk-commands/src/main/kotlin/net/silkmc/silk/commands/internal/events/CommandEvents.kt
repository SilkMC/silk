package net.silkmc.silk.commands.internal.events

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.CommandSelection
import net.silkmc.silk.commands.ClientCommandSourceStack
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.internal.events.Event

@InternalSilkApi
object CommandEvents {
    class Register(
        val dispatcher: CommandDispatcher<CommandSourceStack>,
        val context: CommandBuildContext,
        val selection: CommandSelection
    ) {
        companion object : Event<Register>()
    }

    class RegisterClient(
        val dispatcher: CommandDispatcher<ClientCommandSourceStack>,
        val context: CommandBuildContext,
    ) {
        companion object : Event<RegisterClient>()
    }
}
