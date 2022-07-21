package net.silkmc.silk.commands.registration

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.commands.CommandSourceStack
import net.silkmc.silk.commands.ClientCommandSourceStack
import net.silkmc.silk.commands.RegistrableCommand
import net.silkmc.silk.commands.event.Command
import net.silkmc.silk.core.event.Events

/**
 * Set up a callback which automatically registers this command on server startup.
 */
fun LiteralArgumentBuilder<CommandSourceStack>.setupRegistrationCallback() {
    Events.Command.register.listen { event ->
        event.dispatcher.register(this)
    }
}

/**
 * Set up a callback which automatically registers this command on server startup.
 */
fun RegistrableCommand<CommandSourceStack>.setupRegistrationCallback() {
    Events.Command.register.listen { event ->
        commandBuilder.toBrigadier(event.context).forEach {
            event.dispatcher.root.addChild(it)
        }
    }
}

@Environment(EnvType.CLIENT)
@JvmName("setupRegistrationCallbackClient")
fun LiteralArgumentBuilder<ClientCommandSourceStack>.setupRegistrationCallback() {
    Events.Command.registerClient.listen { event ->
        event.dispatcher.register(this)
    }
}

/**
 * Register this command (client-side).
 */
@Environment(EnvType.CLIENT)
@JvmName("setupRegistrationCallbackClient")
fun RegistrableCommand<ClientCommandSourceStack>.setupRegistrationCallback() {
    Events.Command.registerClient.listen { event ->
        commandBuilder.toBrigadier(event.context).forEach {
            event.dispatcher.root.addChild(it)
        }
    }
}

@Deprecated(
    message = "Registering client commands is now callback based as well.",
    replaceWith = ReplaceWith("setupRegistrationCallback()")
)
fun LiteralArgumentBuilder<ClientCommandSourceStack>.register() = setupRegistrationCallback()

@Deprecated(
    message = "Registering client commands is now callback based as well.",
    replaceWith = ReplaceWith("setupRegistrationCallback()")
)
fun RegistrableCommand<ClientCommandSourceStack>.register() = setupRegistrationCallback()
