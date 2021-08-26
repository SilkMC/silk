package net.axay.fabrik.commands.registration

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource

/**
 * Set up a callback which automatically registers
 * this command (serverside).
 */
fun LiteralArgumentBuilder<ServerCommandSource>.setupRegistrationCallback() {
    CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        dispatcher.register(this)
    }
}

/**
 * Register this command (clientside).
 */
@Environment(EnvType.CLIENT)
fun LiteralArgumentBuilder<FabricClientCommandSource>.register() {
    ClientCommandManager.DISPATCHER.register(this)
}
