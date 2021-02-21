package net.axay.fabrik.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource

/**
 * Set up a callback which automatically registers
 * this command.
 */
fun LiteralArgumentBuilder<ServerCommandSource>.setupRegistrationCallback() {
    CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        dispatcher.register(this)
    }
}
