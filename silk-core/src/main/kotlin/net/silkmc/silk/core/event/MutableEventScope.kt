package net.silkmc.silk.core.event

interface MutableEventScope {
    object Empty : MutableEventScope

    class Cancellable : MutableEventScope {
        var isCancelled: Boolean = false
    }
}
