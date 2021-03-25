package net.axay.fabrik.igui.observable

import kotlin.reflect.KProperty

class GuiProperty<T>(
    private var value: T
) {
    val listeners = HashSet<(T) -> Unit>()

    internal fun invokeListeners() = listeners.forEach { it.invoke(value) }

    operator fun getValue(thisRef: Any?, property: KProperty<*>?) = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>?, value: T) {
        this.value = value
        invokeListeners()
    }
}
