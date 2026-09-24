package net.silkmc.silk.paper.events.internal

import com.mojang.brigadier.CommandDispatcher
import io.papermc.paper.command.brigadier.PaperCommands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.silkmc.silk.commands.event.CommandEvents
import net.silkmc.silk.paper.internal.SilkPaperEntrypoint

fun CommandEvents.setupPaper() {
    SilkPaperEntrypoint.instance.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
        val registrar = event.registrar() as PaperCommands

        @Suppress("UNCHECKED_CAST")
        val dispatcher = registrar.dispatcherInternal as CommandDispatcher<CommandSourceStack>
        register.invoke(CommandEvents.RegisterEvent(dispatcher, registrar.buildContext, Commands.CommandSelection.DEDICATED))
    }
}