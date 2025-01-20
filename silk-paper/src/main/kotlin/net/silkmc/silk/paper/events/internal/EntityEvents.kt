package net.silkmc.silk.paper.events.internal

import net.minecraft.core.Holder
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import net.silkmc.silk.core.event.EntityEvents
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.paper.conversions.mcEntity
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent

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

    listenSilk<PlayerDeathEvent> {
        val pos = it.entity.location
        val event = EntityEvents.EntityDeathEvent(
            it.entity.mcEntity as LivingEntity,
            DamageSource(
                Holder.direct(
                    DamageType(
                        it.damageSource.damageType.key.key,
                        DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, //TODO: Use DamageTypeRegistryEntry in 1.21.4
                        it.damageSource.foodExhaustion,
                        DamageEffects.valueOf(it.damageSource.damageType.damageEffect.toString())
                    )
                ),
                it.entity.mcEntity,
                it.entity.mcEntity,
                Vec3(pos.x, pos.y, pos.z),
            )
        )
        onDeath.invoke(event)
    }
}
