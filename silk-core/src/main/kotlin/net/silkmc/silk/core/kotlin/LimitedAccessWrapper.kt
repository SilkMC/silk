package net.silkmc.silk.core.kotlin

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.silkmc.silk.core.annotations.InternalSilkApi
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Protects access to the given [value] by wrapping it in this class
 * and only exposing it via the [access] function, which will only be
 * executed in a context where the internal [Mutex] is locked.
 */
@InternalSilkApi
class LimitedAccessWrapper<T>(
    @PublishedApi
    internal val value: T,
) {
    @PublishedApi
    internal val mutex = Mutex()

    @OptIn(ExperimentalContracts::class)
    suspend inline fun <R> access(crossinline block: suspend (T) -> R): R {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        return mutex.withLock { block(value) }
    }
}
