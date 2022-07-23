package net.silkmc.silk.core.event

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

/**
 * Events related to an [net.minecraft.world.entity.Entity].
 */
@Suppress("unused")
val Events.entity get() = EntityEvents

@Suppress("unused")
@ExperimentalSilkApi
object EntityEvents {

    open class EntityEvent(open val entity: Entity)
    class EntityDamageEvent(val source: DamageSource, val amount: Float, val target: Entity)

    /**
     * Called when a entity takes damage
     */
    val damage = Event.syncAsync<EntityDamageEvent, EventScope.Cancellable> {
        EventScope.Cancellable()
    }
}
