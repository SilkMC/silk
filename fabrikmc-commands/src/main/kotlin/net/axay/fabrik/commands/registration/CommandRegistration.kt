package net.axay.fabrik.commands.registration

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.axay.fabrik.commands.RegistrableCommand
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack

/**
 * Set up a callback which automatically registers this command on server startup.
 */
fun LiteralArgumentBuilder<CommandSourceStack>.setupRegistrationCallback() {
    CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
        dispatcher.register(this)
    }
}

/**
 * Set up a callback which automatically registers this command on server startup.
 */
fun RegistrableCommand<CommandSourceStack>.setupRegistrationCallback() {
    CommandRegistrationCallback.EVENT.register { dispatcher, context, _ ->
        dispatcher.register(this.commandBuilder.toBrigadier(context))
    }
}

@Environment(EnvType.CLIENT)
@JvmName("setupRegistrationCallbackClient")
fun LiteralArgumentBuilder<FabricClientCommandSource>.setupRegistrationCallback() {
    ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        dispatcher.register(this)
    }
}

/**
 * Register this command (client-side).
 */
@Environment(EnvType.CLIENT)
@JvmName("setupRegistrationCallbackClient")
fun RegistrableCommand<FabricClientCommandSource>.setupRegistrationCallback() {
    ClientCommandRegistrationCallback.EVENT.register { dispatcher, context ->
        dispatcher.register(this.commandBuilder.toBrigadier(context))
    }
}

@Deprecated(
    message = "Registering client commands is now callback based as well.",
    replaceWith = ReplaceWith("setupRegistrationCallback()")
)
fun LiteralArgumentBuilder<FabricClientCommandSource>.register() = setupRegistrationCallback()

@Deprecated(
    message = "Registering client commands is now callback based as well.",
    replaceWith = ReplaceWith("setupRegistrationCallback()")
)
fun RegistrableCommand<FabricClientCommandSource>.register() = setupRegistrationCallback()
