package net.silkmc.silk.core.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.silkmc.silk.core.logging.logger

internal object McCoroutineManager {
    private val log = logger()

    fun init() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            mcCoroutineDispatcher = it.asCoroutineDispatcher()
            mcCoroutineScope = CoroutineScope(SupervisorJob() + mcCoroutineDispatcher)
            log.info("Initialized mcCoroutineScope (MinecraftServer as executor)")
        }
    }

    fun initClient() {
        mcClientCoroutineDispatcher = net.minecraft.client.Minecraft.getInstance().asCoroutineDispatcher()
        mcClientCoroutineScope = CoroutineScope(SupervisorJob() + mcClientCoroutineDispatcher)
        log.info("Initialized mcClientCoroutineScope (MinecraftClient as executor)")
    }
}
