package net.silkmc.silk.paper.events.internal

import net.minecraft.world.entity.LivingEntity
import net.silkmc.silk.core.event.EntityEvents
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.paper.conversions.mcEntity
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.event.entity.EntityDamageEvent

fun EntityEvents.setupPaper() {
    listenSilk<EntityDamageEvent> {
        val mcEntity = it.entity.mcEntity
        checkInvulnerability.invoke(EntityEvents.EntityCheckInvulnerabilityEvent(mcEntity, (it.damageSource as CraftDamageSource).handle, EventScopeProperty(it.isCancelled)))
        if (mcEntity is LivingEntity) {
            damageLivingEntity.invoke(EntityEvents.EntityDamageEvent(mcEntity, it.finalDamage.toFloat(), (it.damageSource as CraftDamageSource).handle))
        }
    }
}