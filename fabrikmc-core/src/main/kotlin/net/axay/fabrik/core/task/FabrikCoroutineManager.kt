package net.axay.fabrik.core.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import net.axay.fabrik.core.logging.logInfo
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

object FabrikCoroutineManager {
    fun init() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            mcCoroutineScope = CoroutineScope(it.asCoroutineDispatcher())
            logInfo("Initialized mcCoroutineScope")
        }
    }
}
