package net.silkmc.silk.core.internal

import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.event.EventPriority
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Server
import net.silkmc.silk.core.logging.logInfo
import net.silkmc.silk.core.task.LifecycleTasksManager
import net.silkmc.silk.core.task.McCoroutineManager

@InternalSilkApi
object SilkEntrypoint {

    fun onInit() {
        logInfo("Initializing Silk")

        Events.Server.preStart.listen(EventPriority.FIRST) {
            Silk.server = it.server
        }

        McCoroutineManager.init()
        LifecycleTasksManager.init()
    }

    fun onInitClient() {
        McCoroutineManager.initClient()
    }
}
