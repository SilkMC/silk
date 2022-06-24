package net.silkmc.silk.core.task

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A [CoroutineDispatcher] which executes code synchronously to the
 * [net.minecraft.server.MinecraftServer] main server thread.
 */
lateinit var mcCoroutineDispatcher: CoroutineDispatcher
    internal set

/**
 * A [CoroutineScope] using the current [net.minecraft.server.MinecraftServer]
 * as the [CoroutineDispatcher].
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
