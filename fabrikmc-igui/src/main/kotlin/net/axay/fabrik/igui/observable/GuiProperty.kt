package net.axay.fabrik.igui.observable

import kotlin.reflect.KProperty

class GuiProperty<T>(
    private var value: T
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}
