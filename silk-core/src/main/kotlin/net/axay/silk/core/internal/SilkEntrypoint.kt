package net.axay.silk.core.internal

import net.axay.silk.core.Silk
import net.axay.silk.core.annotations.InternalSilkApi
import net.axay.silk.core.logging.logInfo
import net.axay.silk.core.task.LifecycleTasksManager
import net.axay.silk.core.task.McCoroutineManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

@InternalSilkApi
class SilkEntrypoint : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        logInfo("Initializing Silk due to init call")

        ServerLifecycleEvents.SERVER_STARTING.register {
            Silk.currentServer = it
            logInfo("Reached SERVER_STARTING state")
        }

        McCoroutineManager.init()
        LifecycleTasksManager.init()
    }

    override fun onInitializeClient() {
        McCoroutineManager.initClient()
    }
}
