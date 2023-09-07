package net.silkmc.silk.core.event

import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A marker which can be passed to event handlers to make the event
 * scope mutable. Functions which mutate the scope will require
 * this marker in the context.
 */
@ExperimentalSilkApi
object MutableEventScope

/**
 * A property which wraps a variable of type [V].
 * Its value can only be mutated inside a [MutableEventScope], which
 * protects it from misuse.
 *
 * See [context-receivers/contextual-delegated-properties KEEP](https://github.com/Kotlin/KEEP/blob/master/proposals/context-receivers.md#contextual-delegated-properties)
 * for why this isn't being used as a delegate *yet*.
 */
@ExperimentalSilkApi
class EventScopeProperty<V>(private var value: V)/* : ReadWriteProperty<Any, V>*/ {

//    context(MutableEventScope)
//    override operator fun getValue(thisRef: Any, property: KProperty<*>): V {
//        return get()
//    }
//
//
//    context(MutableEventScope)
//    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
//        set(value)
//    }

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

interface Cancellable {
    val isCancelled: EventScopeProperty<Boolean>
}
