package net.silkmc.silk.test

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.test.network.NetworkTest

object FabricTestMod : ModInitializer, ClientModInitializer {
    override fun onInitialize() = SilkTest.initServer()

    override fun onInitializeClient() {
        NetworkTest.initClient()

        clientCommand("testmod_client") {
            SilkTest.clientTestmodCommandBuilders.forEach {
                literal(it.key, it.value)
            }
        }
    }
}
