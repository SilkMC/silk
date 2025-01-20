package net.silkmc.silk.test.events

import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Entity
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.logging.logInfo

@OptIn(ExperimentalSilkApi::class)
object EntityEventTest {
    fun init() {
        Events.Entity.onDeath.listen {
            logInfo("received Entity.onDeath event")
        }
    }
}