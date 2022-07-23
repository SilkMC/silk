package net.silkmc.silk.core.event

/**
 * A simple instance class keeping track of a registered listener
 * and making it possible to unregister it later.
 */
sealed class Listener<E, R>(
    private val lock: Any,
    private val list: MutableList<in Listener<E, R>>
) {
    private var registered = false

    /**
     * Tries to register this listener, if it is not already registered.
     */
    fun register() {
        synchronized(lock) {
            if (!registered) {
                registered = true
                list.add(this)
            }
        }
    }

    /**
     * Unregisters this listener, meaning that it won't be called for
     * new event invocations in the future.
     */
    fun unregister() {
        synchronized(lock) {
            if (registered) {
                registered = false
                list.remove(this)
            }
        }
    }

    internal abstract operator fun invoke(scope: EventScope<E, R>): EventScope<E, R>
}

internal class ViewListener<E, R>(
    lock: Any,
    list: MutableList<in Listener<E, R>>,
    private val callback: EventScope<E, R>.() -> Unit,
) : Listener<E, R>(lock, list) {
    override fun invoke(scope: EventScope<E, R>) = scope.also(callback)
}

internal class MutListener<E, R>(
    lock: Any,
    list: MutableList<in Listener<E, R>>,
    private val callback: EventScope<E, R>.() -> R,
) : Listener<E, R>(lock, list) {
    override fun invoke(scope: EventScope<E, R>) = EventScope(scope.event, scope.callback())
}
