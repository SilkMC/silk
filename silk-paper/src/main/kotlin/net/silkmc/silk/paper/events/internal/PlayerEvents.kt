package net.silkmc.silk.paper.events.internal

import io.papermc.paper.adventure.AdventureComponent
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.core.event.PlayerEvents
import net.silkmc.silk.paper.conversions.adventureComponent
import net.silkmc.silk.paper.conversions.mcPlayer
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.craftbukkit.block.CraftBlock
import org.bukkit.event.block.BlockBreakEvent
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
        val event = PlayerEvents.PlayerDeathMessageEvent(it.entity.mcPlayer, EventScopeProperty(AdventureComponent(it.deathMessage())))
        deathMessage.invoke(event)
        it.deathMessage(event.deathMessage.get().adventureComponent)
    }

    listenSilk<BlockBreakEvent> { paperEvent ->
        val craftBlock = paperEvent.block as CraftBlock
        val blockPos = craftBlock.position

        val checkEvent = PlayerEvents.PlayerBlockBreakEvent(
            paperEvent.player.mcPlayer,
            blockPos,
            craftBlock.nms,
            craftBlock.handle.getBlockEntity(blockPos),
            EventScopeProperty(paperEvent.isCancelled)
        )
        blockBreak.invoke(checkEvent)

        if (checkEvent.isCancelled.get()) {
            paperEvent.isCancelled = true
        }
    }
}
