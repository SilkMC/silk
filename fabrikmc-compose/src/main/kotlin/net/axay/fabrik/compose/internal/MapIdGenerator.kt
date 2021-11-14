package net.axay.fabrik.compose.internal

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.axay.fabrik.core.task.fabrikCoroutineScope

internal object MapIdGenerator {
    private val mutex = Mutex()

    private var currentMaxId = Int.MAX_VALUE - 12000

    private val availableOldIds = ArrayList<Int>()

    fun nextId() = runBlocking {
        mutex.withLock {
            availableOldIds.removeFirstOrNull() ?: (++currentMaxId)
        }
    }

    fun makeOldIdAvailable(ids: Collection<Int>) {
        fabrikCoroutineScope.launch {
            mutex.withLock {
                availableOldIds.addAll(ids)
            }
        }
    }
}
