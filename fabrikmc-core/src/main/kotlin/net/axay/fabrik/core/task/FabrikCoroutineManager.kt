package net.axay.fabrik.core.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.axay.fabrik.core.logging.logInfo
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

internal object FabrikCoroutineManager {
    fun init() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            mcCoroutineDispatcher = it.asCoroutineDispatcher()
            mcCoroutineScope = CoroutineScope(SupervisorJob() + mcCoroutineDispatcher)
            logInfo("Initialized mcCoroutineScope")
        }
    }
}
