package net.silkmc.silk.game.cooldown

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.silkmc.silk.core.task.mcCoroutineScope
import java.util.*

/**
 * A key used by all cooldown functions in [net.silkmc.silk.game.cooldown]
 * for storing cooldowns.#
 *
 * @param defaultLength The cooldown length in milliseconds which is used if no other length has been given
 * to the cooldown functions. This defaults to 50ms = 1 tick.
 */
class Cooldown private constructor(val key: String, val defaultLength: Long) {
    constructor(defaultLength: Long = 50L) : this(UUID.randomUUID().toString(), defaultLength)
    constructor(id: ResourceLocation, defaultLength: Long = 50L) : this(id.toString(), defaultLength)

    private val entities = HashSet<Int>()

    /**
     * Checks if this [entity] currently has the given cooldown (specified by [key]).
     */
    fun hasCooldown(entity: Entity) = entities.contains(entity.id)

    /**
     * Applies this cooldown to the given [entity]. After the given [delay], the cooldown
     * will be removed automatically.
     */
    fun applyCooldown(entity: Entity, delay: Long = defaultLength) {
        entities.add(entity.id)
        mcCoroutineScope.launch {
            delay(delay)
            entities.remove(entity.id)
        }
    }

    /**
     * Executes the given [block] **if** the [entity] currently does not have this
     * cooldown. If the [block] is executed, this function will call [applyCooldown]. After
     * the given [delay], the cooldown will be removed automatically.
     */
    inline fun <R> withCooldown(entity: Entity, delay: Long = defaultLength, block: () -> R): R? {
        return if (hasCooldown(entity))
            null
        else {
            applyCooldown(entity, delay)
            block()
        }
    }
}
