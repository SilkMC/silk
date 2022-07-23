@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.core.event

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.annotations.InternalSilkApi
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
open class Event<T, S : EventScope> {

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
     * over the other listeners, see [EventPriority]
     */
    fun listen(priority: EventPriority = EventPriority.NORMAL, callback: context(S, MutableEventScope) (T) -> Unit) {
        synchronized(this) {
            listenersByPriority[priority.ordinal].add(callback)
        }
    }

    /**
     * Monitors this event. This is the same as [listen], but you do not have access
     * to the mutable functions of the event scope. This [callback] will be executed
     * **after** [EventPriority.LAST], therefore monitor sees the final state.
     */
    fun monitor(callback: context(S) (T) -> Unit) {
        monitorListeners.add(callback)
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

    companion object {

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation.
         * The [MutableScope] passed to this function determines what kind of actions
         * can be performed in response to the event.
         */
        fun <ImmutableType, MutableScope : EventScope> syncAsync(clientSide: Boolean = false) =
            AsyncEvent<ImmutableType, MutableScope>(clientSide)

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation.
         * The event passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <ImmutableType> syncAsyncImmutable(clientSide: Boolean = false) =
            AsyncEvent.Immutable<ImmutableType>(clientSide)

        /**
         * Creates a classic [Event] without async listener invocation.
         * The [MutableScope] passed to this function determines what kind of actions
         * can be performed in response to the event.
         */
        fun <T, MutableScope : EventScope> onlySync() =
            Event<T, MutableScope>()

        /**
         * Creates a classic [Event] without async listener invocation.
         * The event scope passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <T> onlySyncImmutable() =
            Immutable<T>()
    }

    class Immutable<T> : Event<T, EventScope.Empty>() {
        fun invoke(instance: T) = invoke(instance, EventScope.Empty)
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
open class AsyncEvent<T, S : EventScope>(val clientSide: Boolean) : Event<T, S>() {

    /**
     * The internal [flow] to which events are [emitted][MutableSharedFlow.emit].
     * It has no buffer, so new calls to [MutableSharedFlow.emit] will suspend if
     * the listeners have not finished handling the previous event.
     */
    @InternalSilkApi
    open val flow = MutableSharedFlow<T>()

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
    fun collectInScope(collector: FlowCollector<T>): Job = launch {
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
    fun collectInScopeSync(collector: FlowCollector<T>): Job = launch(syncDispatcher) {
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
    suspend fun collect(collector: FlowCollector<T>): Nothing {
        flow.collect(collector)
    }

    override fun invoke(instance: T, scope: S) {
        super.invoke(instance, scope)
        invokeScope.launch {
            flow.emit(instance)
        }
    }

    class Immutable<T>(clientSide: Boolean) : AsyncEvent<T, EventScope.Empty>(clientSide) {
        fun invoke(instance: T) = invoke(instance, EventScope.Empty)
    }
}
