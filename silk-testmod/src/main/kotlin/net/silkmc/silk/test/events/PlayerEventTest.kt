package net.silkmc.silk.test.events

import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Player
import net.silkmc.silk.core.logging.logInfo

@OptIn(ExperimentalSilkApi::class)
object PlayerEventTest {
    fun init() {
        Events.Player.onDeath.listen {
            logInfo("received Player.onDeath event")
        }

        Events.Player.onChat.listen {
            logInfo("received Player.onChat event")
        }

        Events.Player.preLogin.listen {
            logInfo("received Player.preLogin event")
        }

        Events.Player.preQuit.listen {
            logInfo("received Player.preQuit event")
        }

        Events.Player.postLogin.listen {
            logInfo("received Player.postLogin event")
        }
    }
}