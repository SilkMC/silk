package net.silkmc.silk.core.task

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.minecraft.server.MinecraftServer
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.event.EventPriority
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Server

@PublishedApi
internal object LifecycleTasksManager {
    val uninitializedServerDeferred = CompletableDeferred<Boolean>()

    fun init() {
        Events.Server.preStart.listen(EventPriority.LAST) {
            uninitializedServerDeferred.complete(true)
        }
    }
}

/**
 * Returns a `Deferred<T>` which will be completed as soon as the server is
 * starting.
 */
@Suppress("DeferredIsResult")
inline fun <T> initWithServerSync(crossinline block: suspend CoroutineScope.(MinecraftServer) -> T) =
    silkCoroutineScope.async {
        LifecycleTasksManager.uninitializedServerDeferred.await()
        mcCoroutineScope.launch {
            block(Silk.currentServer!!)
        }
    }

/**
 * Returns a `Deferred<T>` which will be completed as soon as the server is
 * starting.
 */
inline fun <T> initWithServerAsync(crossinline block: suspend CoroutineScope.(MinecraftServer) -> T) =
    silkCoroutineScope.async {
        LifecycleTasksManager.uninitializedServerDeferred.await()
        block(Silk.currentServer!!)
    }
