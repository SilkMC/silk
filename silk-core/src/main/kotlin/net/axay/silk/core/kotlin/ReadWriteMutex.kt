package net.axay.silk.core.kotlin

import kotlinx.coroutines.sync.Mutex

class ReadWriteMutex {
    private var readers = 0

    private val readCheckMutex = Mutex()
    private val writeMutex = Mutex()

    suspend fun beginRead() {
        readCheckMutex.lock()
        readers++
        if (readers == 1)
            writeMutex.lock()
        readCheckMutex.unlock()
    }

    suspend fun endRead() {
        readCheckMutex.lock()
        readers--
        if (readers == 0)
            writeMutex.unlock()
        readCheckMutex.unlock()
    }

    suspend fun beginWrite() {
        writeMutex.lock()
    }

    fun endWrite() {
        writeMutex.unlock()
    }

    suspend inline fun <T> read(block: () -> T): T {
        beginRead()
        try {
            return block()
        } finally {
            endRead()
        }
    }

    suspend inline fun <T> write(block: () -> T): T {
        beginWrite()
        try {
            return block()
        } finally {
            endWrite()
        }
    }
}
