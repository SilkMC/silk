package net.axay.fabrik.core.task

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A [CoroutineDispatcher] which executes code synchronously to the
 * MinecraftServer main thread.
 */
lateinit var mcCoroutineDispatcher: CoroutineDispatcher
    internal set

/**
 * A [CoroutineScope] using the current MinecraftServer
 * as the Dispatcher.
 */
lateinit var mcCoroutineScope: CoroutineScope
    internal set

/**
 * Does the same as [launch], but the dispatcher defaults to [mcCoroutineDispatcher].
 *
 * This way, you can execute code synchronously (to the MinecraftServer main thread)
 * very easily.
 *
 * ```kotlin
 *  coroutineScope {
 *      mcSyncLaunch {
 *          // suspending and sync now
 *      }
 *  }
 * ```
 */
fun CoroutineScope.mcSyncLaunch(block: suspend CoroutineScope.() -> Unit) =
    launch(mcCoroutineDispatcher, block = block)
