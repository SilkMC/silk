@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.core.event

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
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
open class Event<E, R>(val resultSupplier: () -> R) {

    /**
     * The listeners added by [listen], sorted by [EventPriority] via the index
     * in the outer list.
     */
    @InternalSilkApi
    val listenersByPriority: List<MutableList<Listener<E, R>>> =
        List(EventPriority.values().size) { ArrayList() }

    /**
     * The listeners added by [monitor].
     */
    @InternalSilkApi
    val monitorListeners: MutableList<Listener<E, R>> = ArrayList()

    /**
     * Listens to this event. The [callback] will always be called synchronously.
     * This function is synchronized, so it may be called from any thread.
     *
     * @param priority specifies the priority with which this listener will be called
     * over the other listeners, see [EventPriority] - for listeners which should be
     * invoked synchronously after all mutations have taken place, see [monitor]
     * @param register if true (the default), the listener will be register immediately
     *
     * @return the [Listener], making it possible to register and unregister
     * the listener later
     */
    fun listen(
        priority: EventPriority = EventPriority.NORMAL,
        register: Boolean = true,
        callback: context(EventScope<E, R>) () -> Unit,
    ): Listener<E, R> = ViewListener(this, listenersByPriority[priority.ordinal], callback)
        .also { if (register) it.register() }

    fun listenMut(
        priority: EventPriority = EventPriority.NORMAL,
        register: Boolean = true,
        callback: context(EventScope<E, R>) () -> R,
    ): Listener<E, R> = MutListener(this, listenersByPriority[priority.ordinal], callback)
        .also { if (register) it.register() }

    /**
     * Monitors this event. This is the same as [listen], but you do not have access
     * to the mutable functions of the event scope. This [callback] will be executed
     * **after** [EventPriority.LAST], therefore monitor sees the final state.
     */
    fun monitor(
        register: Boolean = true,
        callback: context(EventScope<E, R>) () -> Unit,
    ): Listener<E, R> = ViewListener(this, monitorListeners, callback)
        .also { if (register) it.register() }

    /**
     * Invokes this event. Calling this function will trigger all
     * listeners and collectors.
     */
    open fun invoke(instance: E, initialResult: R) {
        synchronized(this) {
            var scope = EventScope(instance, initialResult)
            for (listeners in listenersByPriority) {
                for (listener in listeners) {
                    scope = listener(scope)
                }
            }
            for (listener in monitorListeners) {
                listener(scope)
            }
        }
    }

    /**
     * Same as [invoke], but it uses the default initial result provided by [resultSupplier].
     * Returns the final result.
     */
    fun invoke(instance: E): R {
        val result = resultSupplier()
        invoke(instance, result)
        return result
    }

    companion object {

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation, accepting events of the type [T].
         * The scope [S] passed to this function determines what kind of actions
         * can be performed in response to the event.
         *
         * @param resultSupplier creates the default scope for this event
         */
        fun <E, R> syncAsync(clientSide: Boolean = false, resultSupplier: () -> R) =
            AsyncEvent<E, R>(clientSide, resultSupplier)

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation, accepting events of the type [E].
         * The event scope passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <E> syncAsyncImmutable(clientSide: Boolean = false) =
            AsyncEvent<E, Unit>(clientSide, resultSupplier = { })

        /**
         * Creates a classic [Event] without async listener invocation,
         * accepting events of the type [E].
         * The scope [R] passed to this function determines what kind of actions
         * can be performed in response to the event.
         *
         * @param resultSupplier creates the default scope for this event
         */
        fun <E, R> onlySync(resultSupplier: () -> R) =
            Event<E, R>(resultSupplier)

        /**
         * Creates a classic [Event] without async listener invocation,
         * accepting events of the type [E].
         * The event scope passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <E> onlySyncImmutable() = Event<E, Unit>(resultSupplier = { })
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
open class AsyncEvent<E, R>(val clientSide: Boolean, resultSupplier: () -> R) : Event<E, R>(resultSupplier) {

    /**
     * The internal [flow] to which events are [emitted][MutableSharedFlow.emit].
     * It has no buffer, so new calls to [MutableSharedFlow.emit] will suspend if
     * the listeners have not finished handling the previous event.
     */
    @InternalSilkApi
    open val flow = MutableSharedFlow<E>()

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
    fun collectInScope(collector: FlowCollector<E>): Job = launch {
        flow.collect(collector)
    }

    /**
     * Listens to this event, and `emit`s to the [collector] if
     * it is called.
     * Launches [SharedFlow.collect] on the underlying [flow] in the current
     * [CoroutineScope] (taken from context), using the fitting synchronous
     * Minecraft main thread dispatcher ([syncDispatcher]).
     */
    context(CoroutineScope)
    fun collectInScopeSync(collector: FlowCollector<E>): Job = launch(syncDispatcher) {
        flow.collect(collector)
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
    suspend fun collect(collector: FlowCollector<E>): Nothing {
        flow.collect(collector)
    }

    override fun invoke(instance: E, initialResult: R) {
        super.invoke(instance, initialResult)
        invokeScope.launch {
            flow.emit(instance)
        }
    }
}
