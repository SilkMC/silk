package net.axay.fabrik.core

import net.axay.fabrik.core.task.FabrikCoroutineManager
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

object Fabrik {

    /**
     * You should call this function if you intend to
     * use the Fabrik API.
     */
    fun init() {
        println("Initializing Fabrik due to init call...")

        ServerLifecycleEvents.SERVER_STARTING.register {
            println("Reached SERVER_STARTING state")
        }

        FabrikCoroutineManager.init()
    }

}
