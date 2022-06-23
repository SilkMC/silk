package net.silkmc.silk.core.internal.events

import net.silkmc.silk.core.annotations.InternalSilkApi

@InternalSilkApi
open class Event<T> {
    private val listeners = ArrayList<(T) -> Unit>()

    fun register(callback: (event: T) -> Unit) {
        listeners += callback
    }

    fun invoke(instance: T) {
        listeners.forEach {
            it(instance)
        }
    }
}
