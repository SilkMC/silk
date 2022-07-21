package net.silkmc.silk.core.event

interface EventScope {
    object Empty : EventScope

    class Cancellable : EventScope {
        var isCancelled: Boolean = false
    }
}
