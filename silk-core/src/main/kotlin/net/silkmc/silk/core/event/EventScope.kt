package net.silkmc.silk.core.event

import net.silkmc.silk.core.annotations.ExperimentalSilkApi

/**
 * A marker which can be passed to event handlers to make the event
 * scope mutable. Functions which mutate the scope will require
 * this marker in the context.
 */
@ExperimentalSilkApi
object MutableEventScope

/**
 * An event scope can provide additional information and / or
 * functions to event handlers without the event itself having
 * to implement these features.
 * The prime example for this is cancellation, see [Cancellable].
 */
@ExperimentalSilkApi
interface EventScope {

    /**
     * An empty [EventScope], this is simply a no-op implementation.
     */
    object Empty : EventScope

    /**
     * A simple [EventScope] providing the [isCancelled] property.
     */
    open class Cancellable : EventScope {

        /**
         * If set to `true`, further execution of the action why this event
         * was invoked will be cancelled.
         *
         * The default value of this property is `false`.
         */
        val isCancelled = EventScopeProperty(false)
    }
}

@ExperimentalSilkApi
class EventScopeProperty<V>(private var value: V) {

    /**
     * Returns the current value of this property.
     */
    fun get(): V {
        return value
    }

    /**
     * Mutates this property. This functions **must** be called in a
     * [MutableEventScope], therefore in a normal synchronous listener.
     */
    context(MutableEventScope)
    fun set(value: V) {
        this.value = value
    }
}
