package net.silkmc.silk.test.events

import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.PlayerConnection
import net.silkmc.silk.core.logging.logInfo
import net.silkmc.silk.core.text.literalText

@OptIn(ExperimentalSilkApi::class)
object PlayerEventTest {
    fun init() {
        Events.PlayerConnection.playerJoin.listen {
            it.joinMessage = literalText {
                text(it.player.gameProfile.name) {
                    color = 0x3424
                }
                text(" joined the game (this is a custom join message!)") {
                    color = 0x2334
                }
            }
        }
    }
}