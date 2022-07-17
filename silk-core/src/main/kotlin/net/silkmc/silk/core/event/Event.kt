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
open class Event<T> {

    @InternalSilkApi
    val listeners: MutableList<(T) -> Unit> = ArrayList()

    /**
     * Listens to this event. The [callback] will always be called synchronously.
     * This function is synchronized, so it may be called from any thread.
     */
    fun listen(callback: (event: T) -> Unit) {
        synchronized(this) {
            listeners += callback
        }
    }

    /**
     * Invokes this event. Calling this function will trigger all
     * listeners and collectors.
     */
    open fun invoke(instance: T) {
        synchronized(this) {
            listeners.forEach { it.invoke(instance) }
        }
    }

    companion object {

        /**
         * Creates an [AsyncEvent], with different types for async and synchronous
         * listeners.
         */
        fun <ImmutableType, MutableType : ImmutableType> both() =
            AsyncEvent<ImmutableType, MutableType>()

        /**
         * Creates an [AsyncEvent], where all event instances passed to any listener
         * will always be immutable.
         */
        fun <ImmutableType> bothImmutable() =
            AsyncEvent<ImmutableType, ImmutableType>()

        /**
         * Creates a classic [Event] without async listener invocation.
         * The event instance type [T] can be both mutable or immutable.
         */
        fun <T> onlySync() =
            Event<T>()
    }
}

@ExperimentalSilkApi
open class AsyncEvent<ImmutableType, MutableType : ImmutableType> : Event<MutableType>() {

    /**
     * The internal [flow] to which events are [emitted][MutableSharedFlow.emit].
     * It has no buffer, so new calls to [MutableSharedFlow.emit] will suspend if
     * the listeners have not finished handling the previous event.
     */
    @InternalSilkApi
    open val flow = MutableSharedFlow<ImmutableType>()

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
    fun collectInScope(collector: FlowCollector<ImmutableType>): Job = launch {
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
    fun collectInScopeSync(collector: FlowCollector<ImmutableType>): Job = launch(syncDispatcher) {
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
    suspend fun collect(collector: FlowCollector<ImmutableType>): Nothing {
        flow.collect(collector)
    }

    override fun invoke(instance: MutableType) {
        super.invoke(instance)
        invokeScope.launch {
            flow.emit(instance)
        }
    }
}
