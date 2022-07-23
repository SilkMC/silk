package net.silkmc.silk.core.event

/**
 * A simple instance class keeping track of a registered listener
 * and making it possible to unregister it later.
 */
class ListenerInstance<L>(
    private val lock: Any,
    private val listener: L,
    private val list: MutableList<L>,
) {

    private var registered = false

    /**
     * Unregisters this listener, meaning that it won't be called for
     * new event invocations in the future.
     */
    fun unregister() {
        synchronized(lock) {
            if (registered) {
                registered = false
                list.remove(listener)
            }
        }
    }

    /**
     * Tries to register this listener, if it is not already registered.
     */
    fun register() {
        synchronized(lock) {
            if (!registered) {
                registered = true
                list.add(listener)
            }
        }
    }
}
