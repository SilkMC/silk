package net.axay.fabrik.core.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

object FabrikCoroutineManager {

    fun init() {
        println("Initializing FabrikCoroutineManager due to init call...")

        ServerLifecycleEvents.SERVER_STARTING.register {
            println("Initializing mcCoroutineScope due to SERVER_STARTING event...")
            mcCoroutineScope = CoroutineScope(it.asCoroutineDispatcher())
        }
    }

}
