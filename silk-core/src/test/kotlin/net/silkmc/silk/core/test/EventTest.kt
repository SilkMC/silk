package net.silkmc.silk.core.test

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.mpp.log
import kotlinx.coroutines.*
import net.silkmc.silk.core.event.Cancellable
import net.silkmc.silk.core.event.Event
import net.silkmc.silk.core.event.EventPriority
import net.silkmc.silk.core.event.EventScopeProperty
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class EventTest : FunSpec({
    context("create event instances") {
        test("create onlySync instance") {
            Event.onlySync<EventClass>()
            Event.onlySync<EventClassCancellable>()
        }

        test("create syncAsync instances") {
            Event.syncAsync<EventClass>()
            Event.syncAsync<EventClassCancellable>()
        }
    }

    context("invoke events") {
        context("synchronous listener") {
            suspend fun testListenReceive(priority: EventPriority) {
                test("listen to events with priority = $priority and invoke") {
                    val onlySync = Event.onlySync<EventClass>()
                    val onlySyncCancellable = Event.onlySync<EventClassCancellable>()

                    val syncEvents = listOf(onlySync, onlySyncCancellable)

                    fun invokeEvents(message: String = "test message") {
                        onlySync.invoke(EventClass(message))
                        onlySyncCancellable.invoke(EventClassCancellable(message))
                    }

                    var received = 0

                    syncEvents.forEach {
                        it.listen(priority) {
                            log { "triggered $priority event listener, message: ${it.message}" }
                            received++
                        }
                    }

                    invokeEvents("test invoked $priority listeners")
                    received shouldBe syncEvents.size
                }
            }

            testListenReceive(EventPriority.NORMAL)
            EventPriority.entries.forEach {
                testListenReceive(it)
            }
        }

        context("asynchronous collector") {
            suspend fun testListenReceive(priority: EventPriority) {
                test("collect async events with priority = $priority and invoke") {
                    val syncAsync = Event.syncAsync<EventClass>()
                    val syncAsyncCancellable = Event.syncAsync<EventClassCancellable>()

                    val asyncEvents = listOf(syncAsyncCancellable, syncAsync)

                    fun invokeEvents(message: String = "test message") {
                        syncAsync.invoke(EventClass(message))
                        syncAsyncCancellable.invoke(EventClassCancellable(message))
                    }

                    val futures = asyncEvents.associateWith { CompletableDeferred<Unit>() }

                    CoroutineScope(Dispatchers.Default).launch {
                        futures.forEach { (event, deferred) ->
                            event.collectInScope {
                                log { "triggered $priority event collector, message: ${it.message}" }
                                deferred.complete(Unit)
                            }
                        }
                    }
                    delay(100.milliseconds)

                    invokeEvents("test invoked $priority collectors")

                    shouldNotThrow<TimeoutCancellationException> {
                        withTimeout(5.seconds) {
                            futures.values.forEach { it.await() }
                        }
                    }
                }
            }

            testListenReceive(EventPriority.NORMAL)
            EventPriority.entries.forEach {
                testListenReceive(it)
            }
        }
    }
}) {

    open class EventClass(val message: String)

    class EventClassCancellable(message: String) : EventClass(message), Cancellable {
        override val isCancelled = EventScopeProperty(false)
    }
}
