package net.silkmc.silk.core.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.server
import net.silkmc.silk.core.logging.logger

internal object McCoroutineManager {
    private val log = logger()

    fun init() {
        Events.server.preStart.listen {
            mcCoroutineDispatcher = it.server.asCoroutineDispatcher()
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
