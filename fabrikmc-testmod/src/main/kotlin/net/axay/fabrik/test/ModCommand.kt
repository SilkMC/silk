package net.axay.fabrik.test

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource

abstract class ModCommand {

    abstract val command: LiteralArgumentBuilder<ServerCommandSource>

    init {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(command)
        }
    }

}
