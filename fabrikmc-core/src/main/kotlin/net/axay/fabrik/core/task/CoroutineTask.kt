package net.axay.fabrik.core.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A CoroutineScope using the current MinecraftServer
 * as the Dispatcher.
 */
lateinit var mcCoroutineScope: CoroutineScope
    internal set

/**
 * A CoroutineScope using the IO Dispatcher
 * of kotlinx.coroutines.
 */
val fabrikCoroutineScope = CoroutineScope(Dispatchers.IO)

/**
 * Simplifies the usage with coroutines, if you
 * just want to sync something to the main server
 * thread.
 *
 * @param sync if true, the coroutine will be executed by the
 * MinecraftServer itself
 * @param howOften specifies how often the task should be executed
 * @param period the time (in ms) between each "round" of execution
 * @param delay the delay (in ms) for the task to start
 * @param task the task which should be executed
 *
 * @return the CoroutineTask
 */
inline fun coroutineTask(
    sync: Boolean = true,
    howOften: Long = 1,
    period: Long = 0,
    delay: Long = 0,
    crossinline task: suspend (CoroutineTask) -> Unit
): CoroutineTask {
    val coroutineTask = CoroutineTask()
    (if (sync) mcCoroutineScope else fabrikCoroutineScope).launch {
        delay(delay)
        for (i in 1 .. howOften) {
            coroutineTask.round = i

            if (coroutineTask.isCancelled)
                break

            task.invoke(coroutineTask)

            if (i < howOften)
                delay(period)
        }
    }
    return coroutineTask
}

class CoroutineTask(
    /**
     * The current round.
     */
    var round: Long = 0,
    /**
     * Set this to true, if you wish to cancel the repeating execution of the task.
     */
    var isCancelled: Boolean = false
)
