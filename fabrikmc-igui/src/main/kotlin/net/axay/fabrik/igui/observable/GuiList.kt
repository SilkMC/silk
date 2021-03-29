package net.axay.fabrik.igui.observable

class GuiList<T>(val internalCollection: MutableList<T>) {
    val listeners = HashSet<(List<T>) -> Unit>()

    fun invokeListeners() = listeners.forEach { it.invoke(internalCollection) }

    inline fun mutate(action: (MutableList<T>) -> Unit) {
        action.invoke(internalCollection)
        invokeListeners()
    }
}

/**
 * Wraps this list in a gui list, which informs
 * gui components about any updates.
 */
fun <T> MutableList<T>.asGuiList() = GuiList(this)

/**
 * Creates a new gui list with the content of
 * this iterable.
 * A gui list informs gui components about any
 * updates.
 */
fun <T> Iterable<T>.toGuiList() = GuiList(this.toMutableList())
