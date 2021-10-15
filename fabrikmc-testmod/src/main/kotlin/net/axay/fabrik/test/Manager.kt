package net.axay.fabrik.test

import net.axay.fabrik.commands.LiteralCommandBuilder
import net.axay.fabrik.commands.command
import net.axay.fabrik.test.commands.*
import net.axay.fabrik.test.network.NetworkTest
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.server.command.ServerCommandSource

object Manager : ModInitializer, ClientModInitializer {
    internal val testmodCommandBuilders = HashMap<String, LiteralCommandBuilder<ServerCommandSource>.() -> Unit>()

    override fun onInitialize() {
        circleCommand
        sphereCommand
        guiCommand
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
    }
}
