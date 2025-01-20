package net.silkmc.silk.core.event

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@ExperimentalSilkApi
object EntityEvents {

    open class EntityEvent<T : Entity>(val entity: T)

    open class EntityDamageEvent(
        entity: LivingEntity,
        val amount: Float,
        val source: DamageSource,
    ) : EntityEvent<LivingEntity>(entity)

    /**
     * Called when a [LivingEntity] is being hurt. When this event occurs,
     * at least one invulnerability check has already passed.
     *
     * This event is (currently) not cancellable, but you can use the
     * [checkInvulnerability] event for controlling who can be hurt.
     */
    val damageLivingEntity = Event.onlySync<EntityDamageEvent>()

    open class EntityCheckInvulnerabilityEvent(
        entity: Entity,
        val source: DamageSource,
        val isInvulnerable: EventScopeProperty<Boolean>,
    ) : EntityEvent<Entity>(entity)

    /**
     * Called when a regular invulnerability check is being performed.
     * This event allows listeners to modify the result of that check.
     *
     * Note: entity specific invulnerability rules (e.g. a digging warden)
     * cannot be changed using this event
     *
     * Note: this event might be called multiple times for the same damage
     * event, since Minecraft performs its checks more than one time
     */
    val checkInvulnerability = Event.onlySync<EntityCheckInvulnerabilityEvent>()

    /**
     * Called when a [LivingEntity] dies.
     */
    open class EntityDeathEvent(
        entity: LivingEntity,
        val damageSource: DamageSource
    ) : EntityEvent<LivingEntity>(entity)

    val onDeath = Event.syncAsync<EntityDeathEvent>()
}
