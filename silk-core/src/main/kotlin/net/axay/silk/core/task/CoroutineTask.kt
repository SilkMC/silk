package net.axay.silk.core.task

import kotlinx.coroutines.*
import net.axay.silk.core.annotations.DelicateSilkApi
import net.axay.silk.core.kotlin.ticks
import kotlin.time.Duration

/**
 * A CoroutineScope using the "Default" dispatcher
 * of kotlinx.coroutines.
 */
@DelicateSilkApi
val silkCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

@Deprecated(
    message = "FabrikMC has been renamed to Silk.",
    replaceWith = ReplaceWith("silkCoroutineScope")
)
val fabrikCoroutineScope = silkCoroutineScope

/**
 * Allows you to use coroutines for tasks, both repeating and non-repeating.
 *
 * @param sync if true, the coroutine will be executed by the Minecraft main thread
 * @param client if true, a dispatcher for syncing with the client main thread will be used
 * @param howOften specifies how often the task should be executed, for infinite execution use [infiniteMcCoroutineTask]
 * @param period the duration between each "round" of execution
 * @param delay the delay before the first execution begins
 * @param task the task which should be executed
 *
 * @return the [Job] for this task, which is cancellable
 */
inline fun mcCoroutineTask(
    sync: Boolean = true,
    client: Boolean = false,
    scope: CoroutineScope = if (sync) { if (client) mcClientCoroutineScope else mcCoroutineScope } else silkCoroutineScope,
    howOften: Long = 1,
    period: Duration = 1.ticks,
    delay: Duration = Duration.ZERO,
    crossinline task: suspend CoroutineScope.(task: CoroutineTask) -> Unit
): Job {
    val coroutineTask = CoroutineTask(howOften)
    return scope.launch {
        delay(delay)
        for (i in 1..howOften) {
            coroutineTask.internalRound = i

            task(this, coroutineTask)

            if (!isActive) break

            if (i < howOften)
                delay(period)
        }
    }
}

@Deprecated(
    message = "Use mcCoroutineTask instead and change the duration parameters type from Long to Duration.",
    replaceWith = ReplaceWith("mcCoroutineTask(sync = sync, howOften = howOften, period = period.milliseconds, delay = delay.milliseconds) { task.invoke() }")
)
inline fun coroutineTask(
    sync: Boolean = true,
    howOften: Long = 1,
    period: Long = 0,
    delay: Long = 0,
    crossinline task: suspend CoroutineScope.(task: CoroutineTask) -> Unit
): Job {
    val coroutineTask = CoroutineTask(howOften)
    return (if (sync) mcCoroutineScope else silkCoroutineScope).launch {
        delay(delay)
        for (i in 1..howOften) {
            coroutineTask.internalRound = i

            task(this, coroutineTask)

            if (!isActive) break

            if (i < howOften)
                delay(period)
        }
    }
}

/**
 * Allows you to use coroutines for infinitely repeating tasks.
 *
 * @param sync if true, the coroutine will be executed by the Minecraft main thread
 * @param client if true, a dispatcher for syncing with the client main thread will be used
 * @param period the duration between each "round" of execution
 * @param delay the delay before the first execution begins
 * @param task the task which should be executed
 *
 * @return the [Job] for this task, which is cancellable
 */
inline fun infiniteMcCoroutineTask(
    sync: Boolean = true,
    client: Boolean = false,
    scope: CoroutineScope = if (sync) { if (client) mcClientCoroutineScope else mcCoroutineScope } else silkCoroutineScope,
    period: Duration = 1.ticks,
    delay: Duration = Duration.ZERO,
    crossinline task: suspend CoroutineScope.() -> Unit,
): Job {
    return scope.launch {
        delay(delay)

        while (isActive) {
            task()
            delay(period)
        }
    }
}

class CoroutineTask(private val howOften: Long) {
    @PublishedApi
    internal var internalRound: Long = 1

    /**
     * The current round.
     *
     * Counts up from 1 to the given value of howOften (inclusive).
     */
    val round get() = internalRound

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
