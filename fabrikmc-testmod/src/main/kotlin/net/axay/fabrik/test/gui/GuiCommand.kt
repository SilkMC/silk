package net.axay.fabrik.test.gui

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.axay.fabrik.igui.*
import net.axay.fabrik.test.ModCommand
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object GuiCommand : ModCommand() {

    override val command: LiteralArgumentBuilder<ServerCommandSource> = CommandManager.literal("gui")
        .then(
            CommandManager.literal("open").executes {

                try {
                    it.source.player.openGui(SimpleTestGui.gui)
                } catch (exc: Throwable) {
                    exc.printStackTrace()
                }

                return@executes 1
            }
        )

}
