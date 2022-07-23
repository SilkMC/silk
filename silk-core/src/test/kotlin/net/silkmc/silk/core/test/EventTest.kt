package net.silkmc.silk.core.test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.mpp.log
import kotlinx.coroutines.*
import net.silkmc.silk.core.event.*
import kotlin.time.Duration.Companion.seconds
import net.silkmc.silk.core.event.Cancellable.Active
import net.silkmc.silk.core.event.Cancellable.Cancelled

class EventTest : FunSpec({
    context("create event instances") {
        test("create onlySync instance") {
            Event.onlySync<EventClass, Cancellable> { Active }
            Event.onlySyncImmutable<EventClass>()
        }

        test("create syncAsync instances") {
            Event.syncAsync<EventClass, Cancellable> { Active }
            Event.syncAsyncImmutable<EventClass>()
        }
    }

    context("invoke events") {
        context("synchronous listener") {
            suspend fun testListenReceive(priority: EventPriority) {
                test("listen to events with priority = $priority and invoke") {
                    val onlySync = Event.onlySync<EventClass, Cancellable> { Active }
                    val onlySyncImmutable = Event.onlySyncImmutable<EventClass>()

                    val syncEvents = listOf(onlySync, onlySyncImmutable)

                    fun invokeEvents(message: String = "test message") {
                        onlySync.invoke(EventClass(message))
                        onlySyncImmutable.invoke(EventClass(message))
                    }

                    var received = 0

                    onlySync.listen(priority) {
                        log { "triggered $priority event listener, message: ${event.message}" }
                    }

                    onlySync.listenMut(priority) {
                        if (result is Active) {
                            log { "not cancelled yet, cancelling" }
                            Cancelled
                        } else result
                    }

                    syncEvents.forEach {
                        it.listen(priority) {
                            // https://youtrack.jetbrains.com/issue/KT-51277/
                            // log { "triggered $priority event listener, message: ${event.message}" }
                            received++
                        }
                    }

                    invokeEvents("test invoked $priority listeners")
                    received shouldBe syncEvents.size
                }
            }

            testListenReceive(EventPriority.NORMAL)
            EventPriority.values().forEach {
                testListenReceive(it)
            }
        }

        context("asynchronous collector") {
            suspend fun testListenReceive(priority: EventPriority) {
                test("collect async events with priority = $priority and invoke") {
                    val syncAsync = Event.syncAsync<EventClass, Cancellable> { Active }
                    val syncAsyncImmutable = Event.syncAsyncImmutable<EventClass>()

                    val asyncEvents = listOf(syncAsync, syncAsyncImmutable)

                    fun invokeEvents(message: String = "test message") {
                        syncAsync.invoke(EventClass(message))
                        syncAsyncImmutable.invoke(EventClass(message))
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

                    delay(500)

                    invokeEvents("test invoked $priority collectors")

                    withTimeout(5.seconds) {
                        futures.values.forEach { it.await() }
                    }
                }
            }

            testListenReceive(EventPriority.NORMAL)
            EventPriority.values().forEach {
                testListenReceive(it)
            }
        }
    }
}) {
    class EventClass(val message: String)
}
