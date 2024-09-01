package net.silkmc.silk.test.events

import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Entity
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Player
import net.silkmc.silk.core.logging.logInfo
import net.silkmc.silk.core.text.literalText

@OptIn(ExperimentalSilkApi::class)
object PlayerEventTest {
    fun init() {
        Events.Player.deathMessage.listen { event ->
            logInfo("received Player.deathMessage event")
            event.deathMessage.set(literalText("This is a test death message"))
        }
    }
}