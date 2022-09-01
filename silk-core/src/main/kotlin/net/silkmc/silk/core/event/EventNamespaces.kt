package net.silkmc.silk.core.event

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@Suppress("UnusedReceiverParameter") // receiver is for namespacing only
val Events.Entity get() = EntityEvents

@ExperimentalSilkApi
object EntityEvents {

    open class EntityEvent(open val entity: Entity)

    class EntityDamageEvent(val source: DamageSource, val amount: Float, override val entity: Entity) : EntityEvent(entity)

    /**
     * Called when a entity takes damage
     */
    val preDamage = Event.syncAsync<EntityDamageEvent, EventScope.Cancellable> {
        EventScope.Cancellable()
    }
}
