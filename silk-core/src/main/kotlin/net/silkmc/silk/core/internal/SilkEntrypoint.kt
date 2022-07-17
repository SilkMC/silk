package net.silkmc.silk.core.internal

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.server
import net.silkmc.silk.core.logging.logInfo
import net.silkmc.silk.core.task.LifecycleTasksManager
import net.silkmc.silk.core.task.McCoroutineManager

@InternalSilkApi
class SilkEntrypoint : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        logInfo("Initializing Silk due to init call")

        Events.server.preStart.listen {
            Silk.currentServer = it.server
            logInfo("Reached SERVER_STARTING state")
        }

        McCoroutineManager.init()
        LifecycleTasksManager.init()
    }

    override fun onInitializeClient() {
        McCoroutineManager.initClient()
    }
}
