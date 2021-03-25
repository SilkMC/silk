package net.axay.fabrik.igui.observable

class GuiList<T>(val collection: MutableList<T>) {
    val listeners = HashSet<(List<T>) -> Unit>()

    fun invokeListeners() = listeners.forEach { it.invoke(collection) }

    inline fun doUpdating(action: (MutableList<T>) -> Unit) {
        action.invoke(collection)
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
