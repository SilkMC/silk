package net.silkmc.silk.compose.internal

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.silkmc.silk.core.task.silkCoroutineScope

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
        silkCoroutineScope.launch {
            mutex.withLock {
                availableOldIds.addAll(ids)
            }
        }
    }
}
