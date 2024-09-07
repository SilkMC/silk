package net.silkmc.silk.test.events

import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Entity
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.logging.logInfo

@OptIn(ExperimentalSilkApi::class)
object EntityEventTest {
    fun init() {
        Events.Entity.dropItem.listen { event ->
            logInfo("received Entity.dropItem event (Item: ${event.item.item.displayName.string})")
        }

        Events.Entity.death.listen { event ->
            logInfo("received Entity.death event (Source: ${event.source}, Target: ${event.entity.type.description.string})")
        }
    }
}