package net.axay.fabrik.core

import net.axay.fabrik.core.logging.logInfo
import net.axay.fabrik.core.task.FabrikCoroutineManager
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

object Fabrik {
    var currentServer: MinecraftServer? = null

    /**
     * You should call this function if you intend to
     * use the Fabrik API.
     */
    fun init() {
        logInfo("Initializing Fabrik due to init call")

        ServerLifecycleEvents.SERVER_STARTING.register {
            currentServer = it
            logInfo("Reached SERVER_STARTING state")
        }

        FabrikCoroutineManager.init()
    }
}
