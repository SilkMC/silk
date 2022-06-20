package net.silkmc.silk.test

import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.commands.command
import net.silkmc.silk.test.commands.*
import net.silkmc.silk.test.network.NetworkTest
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.CommandSourceStack

object Manager : ModInitializer, ClientModInitializer {
    internal val testmodCommandBuilders = HashMap<String, LiteralCommandBuilder<CommandSourceStack>.() -> Unit>()
    internal val clientTestmodCommandBuilders = HashMap<String, LiteralCommandBuilder<FabricClientCommandSource>.() -> Unit>()

    override fun onInitialize() {
        circleCommand
        commandTestCommand
        sphereCommand
        guiCommand
        itemTestCommand
        persistenceTestCommand
        textTestCommand
        sideboardCommand
        NetworkTest.initServer()

        command("testmod") {
            testmodCommandBuilders.forEach {
                literal(it.key, it.value)
            }
        }
    }

    override fun onInitializeClient() {
        NetworkTest.initClient()

        clientCommand("testmodclient") {
            clientTestmodCommandBuilders.forEach {
                literal(it.key, it.value)
            }
        }
    }
}
