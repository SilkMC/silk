package net.silkmc.silk.paper.events.internal

import net.minecraft.world.entity.LivingEntity
import net.silkmc.silk.core.event.BlockEvents
import net.silkmc.silk.paper.conversions.mcBlock
import net.silkmc.silk.paper.conversions.mcEntity
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

fun BlockEvents.setupPaper() {
  listenSilk<BlockBreakEvent> {
    blockBreak.invoke(BlockEvents.BlockEvent(it.player.mcEntity as LivingEntity, it.block.mcBlock ?: return@listenSilk))
  }

  listenSilk<BlockPlaceEvent> {
    blockPlace.invoke(BlockEvents.BlockEvent(it.player.mcEntity as LivingEntity, it.block.mcBlock ?: return@listenSilk))
  }
}