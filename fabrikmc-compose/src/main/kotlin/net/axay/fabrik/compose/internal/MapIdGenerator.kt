package net.axay.fabrik.compose.internal

import java.util.*

object MapIdGenerator {
    private var currentMaxId = Int.MAX_VALUE - 12000
        @Synchronized get
        @Synchronized set

    private val availableOldIds = Vector<Int>()

    fun nextId() = availableOldIds.removeFirstOrNull() ?: ++currentMaxId

    fun makeOldIdAvailable(id: Collection<Int>) = availableOldIds.addAll(id)
}
