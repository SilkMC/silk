@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.core.event

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.event.Event.Companion.onlySync
import net.silkmc.silk.core.event.Event.Companion.onlySyncImmutable
import net.silkmc.silk.core.task.mcClientCoroutineDispatcher
import net.silkmc.silk.core.task.mcCoroutineDispatcher

/**
 * The base object for discovering events, check the extensions
 * values of this object.
 */
@ExperimentalSilkApi
object Events

/**
 * The base event implementation with synchronous listeners.
 * To create a new event of this type, have a look at the
 * [onlySync] and [onlySyncImmutable] functions in the `companion object`.
 */
@ExperimentalSilkApi
open class Event<T, S : EventScope>(val scopeSupplier: () -> S) {

    /**
     * The listeners added by [listen], sorted by [EventPriority] via the index
     * in the outer list.
     */
    @InternalSilkApi
    val listenersByPriority: List<MutableList<context(S, MutableEventScope) (T) -> Unit>> = buildList {
        repeat(EventPriority.values().size) {
            add(ArrayList())
        }
    }

    /**
     * The listeners added by [monitor].
     */
    @InternalSilkApi
    val monitorListeners: MutableList<context(S) (T) -> Unit> = ArrayList()

    /**
     * Listens to this event. The [callback] will always be called synchronously.
     * This function is synchronized, so it may be called from any thread.
     *
     * @param priority specifies the priority with which this listener will be called
     * over the other listeners, see [EventPriority] - for listeners which should be
     * invoked synchronously after all mutations have taken place, see [monitor]
     * @param register if true (the default), the listener will be register immediately
     *
     * @return the [ListenerInstance], making it possible to register and unregister
     * the listener later
     */
    fun listen(
        priority: EventPriority = EventPriority.NORMAL,
        register: Boolean = true,
        callback: context(S, MutableEventScope) (T) -> Unit,
    ): ListenerInstance<*> {
        return ListenerInstance(this, callback, listenersByPriority[priority.ordinal])
            .also { if (register) it.register() }
    }

    /**
     * Monitors this event. This is the same as [listen], but you do not have access
     * to the mutable functions of the event scope. This [callback] will be executed
     * **after** [EventPriority.LAST], therefore monitor sees the final state.
     */
    fun monitor(
        register: Boolean = true,
        callback: context(S) (T) -> Unit,
    ): ListenerInstance<*> {
        return ListenerInstance(this, callback, monitorListeners)
            .also { if (register) it.register() }
    }

    /**
     * Invokes this event. Calling this function will trigger all
     * listeners and collectors.
     */
    open fun invoke(instance: T, scope: S) {
        synchronized(this) {
            for (listeners in listenersByPriority) {
                for (listener in listeners) {
                    listener(scope, MutableEventScope, instance)
                }
            }
            for (listener in monitorListeners) {
                listener(scope, instance)
            }
        }
    }

    /**
     * Same as [invoke], but it uses the default scope provided by [scopeSupplier].
     * Returns the resulting scope.
     */
    fun invoke(instance: T): S {
        val scope = scopeSupplier()
        invoke(instance, scope)
        return scope
    }

    companion object {

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation, accepting events of the type [T].
         * The scope [S] passed to this function determines what kind of actions
         * can be performed in response to the event.
         *
         * @param scopeSupplier creates the default scope for this event
         */
        fun <T, S : EventScope> syncAsync(clientSide: Boolean = false, scopeSupplier: () -> S) =
            AsyncEvent<T, S>(clientSide, scopeSupplier)

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation, accepting events of the type [T].
         * The event scope passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <T> syncAsyncImmutable(clientSide: Boolean = false) =
            AsyncEvent<T, EventScope.Empty>(clientSide, scopeSupplier = { EventScope.Empty })

        /**
         * Creates a classic [Event] without async listener invocation,
         * accepting events of the type [T].
         * The scope [S] passed to this function determines what kind of actions
         * can be performed in response to the event.
         *
         * @param scopeSupplier creates the default scope for this event
         */
        fun <T, S : EventScope> onlySync(scopeSupplier: () -> S) =
            Event<T, S>(scopeSupplier)

        /**
         * Creates a classic [Event] without async listener invocation,
         * accepting events of the type [T].
         * The event scope passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <T> onlySyncImmutable() =
            Event<T, EventScope.Empty>(scopeSupplier = { EventScope.Empty })
    }
}

/**
 * The same as [Event] but with additional async collectors (via a flow).
 * This allows you to listen to the event asynchronously in a totally different
 * [CoroutineScope], and unregister via cancellation of that scope.
 * To create an event of this type, have a look at the [Event.syncAsync] and
 * [Event.syncAsyncImmutable] functions.
 */
@ExperimentalSilkApi
open class AsyncEvent<T, S : EventScope>(val clientSide: Boolean, scopeSupplier: () -> S) : Event<T, S>(scopeSupplier) {

    /**
     * The internal [flow] to which events are [emitted][MutableSharedFlow.emit].
     * It has no buffer, so new calls to [MutableSharedFlow.emit] will suspend if
     * the listeners have not finished handling the previous event.
     */
    @InternalSilkApi
    open val flow = MutableSharedFlow<Pair<T, S>>()

    /**
     * The scope used for emitting events without blocking the current execution.
     */
    @InternalSilkApi
    val invokeScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * The dispatcher used for handling events on the main-thread
     * in a non-blocking way.
     */
    protected open val syncDispatcher: CoroutineDispatcher
        get() = if (clientSide) mcClientCoroutineDispatcher else mcCoroutineDispatcher

    /**
     * Listens to this event, and `emit`s to the [collector] if
     * it is called.
     * Launches [SharedFlow.collect] on the underlying [flow] in the current
     * [CoroutineScope] (taken from context), using the dispatcher from the
     * current scope.
     */
    context(CoroutineScope)
    fun collectInScope(collector: suspend context(S) (T) -> Unit): Job = launch {
        flow.collect {
            collector(it.second, it.first)
        }
    }

    /**
     * Listens to this event, and `emit`s to the [collector] if
     * it is called.
     * Launches [SharedFlow.collect] on the underlying [flow] in the current
     * [CoroutineScope] (taken from context), using the fitting synchronous
     * Minecraft main thread dispatcher ([syncDispatcher]).
     */
    context(CoroutineScope)
    fun collectInScopeSync(collector: suspend context(S) (T) -> Unit): Job = launch(syncDispatcher) {
        flow.collect {
            collector(it.second, it.first)
        }
    }

    /**
     * Calls [SharedFlow.collect] on the underlying [flow].
     * This way the [collector] receives all events.
     * Note that the thread and / or [CoroutineScope] you call this in
     * decides on which thread you handle the events.
     *
     * This function never completes, read [the official collect docs][kotlinx.coroutines.flow.SharedFlow.collect]
     * for more info.
     */
    suspend fun collect(collector: suspend context(S) (T) -> Unit): Nothing {
        flow.collect {
            collector(it.second, it.first)
        }
    }

    override fun invoke(instance: T, scope: S) {
        super.invoke(instance, scope)
        invokeScope.launch {
            flow.emit(instance to scope)
        }
    }
}
