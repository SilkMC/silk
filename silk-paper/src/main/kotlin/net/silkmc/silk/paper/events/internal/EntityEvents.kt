package net.silkmc.silk.paper.events.internal

import net.minecraft.world.entity.LivingEntity
import net.silkmc.silk.core.event.EntityEvents
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.paper.conversions.mcEntity
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.event.entity.EntityDamageEvent

fun EntityEvents.setupPaper() {
    listenSilk<EntityDamageEvent> { paperEvent ->
        val mcEntity = paperEvent.entity.mcEntity
        @Suppress("UnstableApiUsage")
        val mcDamageSource = (paperEvent.damageSource as CraftDamageSource).handle

        val checkEvent = EntityEvents.EntityCheckInvulnerabilityEvent(
            entity = mcEntity,
            source = mcDamageSource,
            isInvulnerable = EventScopeProperty(paperEvent.isCancelled))
        checkInvulnerability.invoke(checkEvent)
        if (checkEvent.isInvulnerable.get() != paperEvent.isCancelled) {
            paperEvent.isCancelled = true
        }

        if (mcEntity is LivingEntity) {
            damageLivingEntity.invoke(EntityEvents.EntityDamageEvent(
                entity = mcEntity,
                amount = paperEvent.finalDamage.toFloat(),
                source = mcDamageSource))
        }
    }
}
