package net.axay.fabrik.core.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.axay.fabrik.core.logging.logger
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

internal object FabrikCoroutineManager {
    private val log = logger()

    fun init() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            mcCoroutineDispatcher = it.asCoroutineDispatcher()
            mcCoroutineScope = CoroutineScope(SupervisorJob() + mcCoroutineDispatcher)
            log.info("Initialized mcCoroutineScope (MinecraftServer as executor)")
        }
    }

    fun initClient() {
        // explicit import required, avoid mentioning this class in a server environment
        mcClientCoroutineDispatcher = net.minecraft.client.MinecraftClient.getInstance().asCoroutineDispatcher()
        mcClientCoroutineScope = CoroutineScope(SupervisorJob() + mcClientCoroutineDispatcher)
        log.info("Initialized mcClientCoroutineScope (MinecraftClient as executor)")
    }
}
