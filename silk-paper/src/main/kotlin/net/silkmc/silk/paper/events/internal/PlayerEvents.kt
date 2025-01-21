package net.silkmc.silk.paper.events.internal

import io.papermc.paper.adventure.AdventureComponent
import net.kyori.adventure.text.Component
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.core.event.PlayerEvents
import net.silkmc.silk.paper.conversions.mcDamageSource
import net.silkmc.silk.paper.conversions.mcPlayer
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.event.entity.PlayerDeathEvent
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

    listenSilk<PlayerDeathEvent> {
        val pos = it.player.location
        val event = PlayerEvents.PlayerDeathEvent(
            it.player.mcPlayer,
            @Suppress("UnstableApiUsage")
            it.damageSource.mcDamageSource,
            EventScopeProperty(AdventureComponent(it.deathMessage()))
        )
        onDeath.invoke(event)
        it.deathMessage(Component.text(event.deathMessage.get().string))
    }
}
