package net.silkmc.silk.core.event

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.Block
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@ExperimentalSilkApi
object BlockEvents {

  open class BlockEvent<T : Entity?>(val entity: T, val block: Block)

  val blockBreak = Event.syncAsync<BlockEvent<LivingEntity>>()

  val blockPlace = Event.syncAsync<BlockEvent<LivingEntity>>()

}