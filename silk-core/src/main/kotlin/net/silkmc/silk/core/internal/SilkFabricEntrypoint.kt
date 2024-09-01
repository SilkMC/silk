package net.silkmc.silk.core.internal

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.silkmc.silk.core.annotations.InternalSilkApi

@InternalSilkApi
class SilkFabricEntrypoint : ModInitializer, ClientModInitializer {

    override fun onInitialize() {
        SilkEntrypoint.onInit()
    }

    override fun onInitializeClient() {
        SilkEntrypoint.onInitClient()
    }
}
