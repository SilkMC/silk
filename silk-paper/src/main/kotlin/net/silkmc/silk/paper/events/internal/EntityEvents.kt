package net.silkmc.silk.paper.events.internal

import net.minecraft.world.entity.LivingEntity
import net.silkmc.silk.core.event.EntityEvents
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.paper.conversions.mcEntity
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.craftbukkit.entity.CraftItem
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityDropItemEvent

fun EntityEvents.setupPaper() {
    listenSilk<EntityDamageEvent> {
        val mcEntity = it.entity.mcEntity

        // Check invulnerability event - start
        val checkInvulnerabilityEvent = EntityEvents.EntityCheckInvulnerabilityEvent(
            mcEntity,
            (it.damageSource as CraftDamageSource).handle,
            EventScopeProperty(false)
        )

        checkInvulnerability.invoke(checkInvulnerabilityEvent)

        if (checkInvulnerabilityEvent.isInvulnerable.get()) {
            it.isCancelled = true
            return@listenSilk
        }
        // Check invulnerability event - end

        // Damage event - start
        if (mcEntity is LivingEntity) {
            damageLivingEntity.invoke(EntityEvents.EntityDamageEvent(mcEntity, it.finalDamage.toFloat(), (it.damageSource as CraftDamageSource).handle))
        }
        // Damage event - end
    }

    listenSilk<EntityDropItemEvent> {
        val dropEvent = EntityEvents.EntityDropItemEvent(
            it.entity.mcEntity,
            (it.itemDrop as CraftItem).handle,
            EventScopeProperty(it.isCancelled)
        )

        dropItem.invoke(dropEvent)

        if (dropEvent.isCancelled.get()) {
            it.isCancelled = true
        }
    }

    listenSilk<EntityDeathEvent> {
        val deathEvent = EntityEvents.EntityDeathEvent(
            it.entity.mcEntity as LivingEntity,
            (it.damageSource as CraftDamageSource).handle,
            EventScopeProperty(it.drops.isNotEmpty()),
            EventScopeProperty(it.isCancelled)
        )

        death.invoke(deathEvent)

        if (deathEvent.isCancelled.get()) {
            it.isCancelled = true
        }

        if (!deathEvent.isDroppingLoot.get()) {
            it.drops.clear()
            it.droppedExp = 0
        }
    }
}