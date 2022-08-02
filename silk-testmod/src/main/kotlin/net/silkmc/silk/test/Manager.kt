package net.silkmc.silk.test

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.commands.CommandSourceStack
import net.silkmc.silk.commands.ClientCommandSourceStack
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.commands.command
import net.silkmc.silk.test.commands.*
import net.silkmc.silk.test.events.ServerEventTest
import net.silkmc.silk.test.network.NetworkTest

object Manager : ModInitializer, ClientModInitializer {
    internal val testmodCommandBuilders = HashMap<String, LiteralCommandBuilder<CommandSourceStack>.() -> Unit>()
    internal val clientTestmodCommandBuilders = HashMap<String, LiteralCommandBuilder<ClientCommandSourceStack>.() -> Unit>()

    override fun onInitialize() {
        circleCommand
        commandTestCommand
        sphereCommand
        guiCommand
        itemTestCommand
        persistenceTestCommand
        textTestCommand
        sideboardCommand
        executeCommandTestCommand
        ServerEventTest.init()
        NetworkTest.initServer()

        command("testmod") {
            testmodCommandBuilders.forEach {
                literal(it.key, it.value)
            }
        }
    }

    override fun onInitializeClient() {
        NetworkTest.initClient()

        clientCommand("testmod_client") {
            clientTestmodCommandBuilders.forEach {
                literal(it.key, it.value)
            }
        }
    }
}
