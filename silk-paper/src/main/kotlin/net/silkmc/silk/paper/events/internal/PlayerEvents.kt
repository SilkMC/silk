package net.silkmc.silk.paper.events.internal

import io.papermc.paper.adventure.AdventureComponent
import net.silkmc.silk.core.event.PlayerEvents
import net.silkmc.silk.paper.conversions.mcPlayer
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

fun PlayerEvents.setupPaper() {
    listenSilk<PlayerLoginEvent> {
        preLogin.invoke(PlayerEvents.PlayerEvent(it.player.mcPlayer))
    }
    listenSilk<PlayerJoinEvent> {
        postLogin.invoke(PlayerEvents.PlayerEvent(it.player.mcPlayer))
    }
    listenSilk<PlayerQuitEvent> {
        preQuit.invoke(PlayerEvents.PlayerQuitEvent(it.player.mcPlayer, AdventureComponent(it.quitMessage())))
    }
}
