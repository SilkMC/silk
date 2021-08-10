package net.axay.fabrik.core.task

import kotlinx.coroutines.*

/**
 * A CoroutineScope using the "Default" dispatcher
 * of kotlinx.coroutines.
 */
val fabrikCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

/**
 * Allows you to use coroutines for tasks which are commonly needed
 * when creating mods.
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
    crossinline task: suspend CoroutineScope.(task: CoroutineTask) -> Unit
): Job {
    val coroutineTask = CoroutineTask(howOften)
    return (if (sync) mcCoroutineScope else fabrikCoroutineScope).launch {
        delay(delay)
        for (i in 1..howOften) {
            coroutineTask.round = i

            task.invoke(this, coroutineTask)

            if (!isActive) break

            if (i < howOften)
                delay(period)
        }
    }
}

class CoroutineTask(
    private val howOften: Long,
    /**
     * The current round.
     *
     * Counts up from 1 to the given value of howOften (inclusive).
     */
    var round: Long = 1,
) {
    /**
     * The current round.
     *
     * Counts up from 0 to the given value of howOften (exclusive).
     */
    val roundFromZero get() = round - 1

    /**
     * Counts down to 1, starting from the given value of howOften (inclusive).
     */
    val counterDownToOne get() = (howOften + 1) - round
    /**
     * Counts down to 0, starting from the given value of howOften (exclusive).
     */
    val counterDownToZero get() = howOften - round
}
