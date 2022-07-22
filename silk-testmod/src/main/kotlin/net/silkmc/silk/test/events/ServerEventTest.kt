package net.silkmc.silk.test.events

import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Server
import net.silkmc.silk.core.logging.logInfo

@OptIn(ExperimentalSilkApi::class)
object ServerEventTest {
    fun init() {
        Events.Server.postStart.listen {
            logInfo("received Server.postStart event")
        }

        Events.Server.preStop.listen {
            logInfo("received Server.preStop event")
        }
    }
}
