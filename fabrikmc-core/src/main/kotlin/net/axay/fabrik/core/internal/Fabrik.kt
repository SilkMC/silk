package net.axay.fabrik.core.internal

import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.logging.logInfo
import net.axay.fabrik.core.task.FabrikCoroutineManager
import net.axay.fabrik.core.task.LifecycleTasksManager
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

/**
 * Do not call this function, as it is the entry point of the Fabrik
 * mod itself.
 */
fun init() {
    logInfo("Initializing Fabrik due to init call")

    ServerLifecycleEvents.SERVER_STARTING.register {
        Fabrik.currentServer = it
        logInfo("Reached SERVER_STARTING state")
    }

    FabrikCoroutineManager.init()
    LifecycleTasksManager.init()
}
