package net.axay.fabrik.core.task

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A [CoroutineDispatcher] which executes code synchronously to the
 * [net.minecraft.client.MinecraftClient] main game thread.
 */
lateinit var mcClientCoroutineDispatcher: CoroutineDispatcher
    internal set

/**
 * A [CoroutineScope] using the current [net.minecraft.client.MinecraftClient]
 * as the [CoroutineDispatcher].
 */
lateinit var mcClientCoroutineScope: CoroutineScope
    internal set

/**
 * Does the same as [launch], but the dispatcher defaults to [mcClientCoroutineDispatcher].
 *
 * This way, you can execute code synchronously (to the MinecraftServer main thread)
 * very easily.
 *
 * ```kotlin
 *  coroutineScope {
 *      mcClientSyncLaunch {
 *          // suspending and sync now
 *      }
 *  }
 * ```
 */
fun CoroutineScope.mcClientSyncLaunch(block: suspend CoroutineScope.() -> Unit) =
    launch(mcClientCoroutineDispatcher, block = block)
