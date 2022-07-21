package net.silkmc.silk.core.event

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.task.mcCoroutineDispatcher

/**
 * The base object for discovering events, check the extensions
 * values of this object.
 */
@ExperimentalSilkApi
object Events

@ExperimentalSilkApi
open class Event<T, MutableScope : MutableEventScope> {

    @InternalSilkApi
    val listeners: MutableList<context(MutableScope) (T) -> Unit> = ArrayList()

    /**
     * Listens to this event. The [callback] will always be called synchronously.
     * This function is synchronized, so it may be called from any thread.
     */
    fun listen(callback: context(MutableScope) (T) -> Unit) {
        synchronized(this) {
            listeners += callback
        }
    }

    /**
     * Invokes this event. Calling this function will trigger all
     * listeners and collectors.
     */
    open fun invoke(instance: T, scope: MutableScope) {
        synchronized(this) {
            listeners.forEach { it.invoke(scope, instance) }
        }
    }

    companion object {

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation.
         * The [MutableScope] passed to this function determines what kind of actions
         * can be performed in response to the event.
         */
        fun <ImmutableType, MutableScope : MutableEventScope> syncAsync() =
            AsyncEvent<ImmutableType, MutableScope>()

        /**
         * Creates an [AsyncEvent] with synchronous and asynchronous
         * listener invocation.
         * The mutable scope passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <ImmutableType> syncAsyncImmutable() =
            AsyncEvent.Immutable<ImmutableType>()

        /**
         * Creates a classic [Event] without async listener invocation.
         * The [MutableScope] passed to this function determines what kind of actions
         * can be performed in response to the event.
         */
        fun <T, MutableScope : MutableEventScope> onlySync() =
            Event<T, MutableScope>()

        /**
         * Creates a classic [Event] without async listener invocation.
         * The mutable scope passed to event handlers will be empty,
         * effectively making the event immutable for all handlers.
         */
        fun <T> onlySyncImmutable() =
            Immutable<T>()
    }

    class Immutable<T> : Event<T, MutableEventScope.Empty>() {
        fun invoke(instance: T) = invoke(instance, MutableEventScope.Empty)
    }
}

@ExperimentalSilkApi
open class AsyncEvent<T, MutableScope : MutableEventScope> : Event<T, MutableScope>() {

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
        get() = mcCoroutineDispatcher

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

    override fun invoke(instance: T, scope: MutableScope) {
        super.invoke(instance, scope)
        invokeScope.launch {
            flow.emit(instance)
        }
    }

    class Immutable<T> : AsyncEvent<T, MutableEventScope.Empty>() {
        fun invoke(instance: T) = invoke(instance, MutableEventScope.Empty)
    }
}
