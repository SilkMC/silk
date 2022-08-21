package net.silkmc.silk.game.sideboard

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.task.silkCoroutineScope
import kotlin.time.Duration

/**
 * This interface represents one line in a [Sideboard].
 */
interface SideboardLine {

    /**
     * The internal text flow, which the sideboard collects in
     * order to receive line updates.
     */
    @InternalSilkApi
    val textFlow: Flow<Component>

    /**
     * A sideboard line which always displays the same [text].
     */
    class Static(text: Component) : SideboardLine {
        override val textFlow = flowOf(text)
    }

    /**
     * Adds a line which changes whenever a new [Component]
     * is emitted to the [flow].
     */
    open class Changing(flow: Flow<Component>) : SideboardLine {
        override val textFlow = flow
    }

    /**
     * An updatable sideboard line, which might have an [initial]
     * content, and can be changed at any time.
     */
    open class Updatable(initial: Component? = null) : SideboardLine {

        override val textFlow = MutableSharedFlow<Component>()

        init {
            if (initial != null) {
                silkCoroutineScope.launch {
                    textFlow.emit(initial)
                }
            }
        }

        suspend fun update(text: Component) {
            textFlow.emit(text)
        }

        fun launchUpdate(text: Component) {
            silkCoroutineScope.launch {
                textFlow.emit(text)
            }
        }
    }

    /**
     * A sideboard line which displays the result of [updater],
     * which will be called periodically by the given [Duration] of [period].
     */
    open class UpdatingPeriodically(
        period: Duration,
        updater: suspend () -> Component,
    ) : SideboardLine {

        override val textFlow = flow {
            while (currentCoroutineContext().isActive) {
                emit(updater())
                delay(period)
            }
        }
    }
}

/**
 * A sideboard line which does not change and always displays the
 * same [Text].
 */
@Deprecated(
    message = "Use SideboardLine.Static instead.",
    replaceWith = ReplaceWith("SideboardLine.Static")
)
class SimpleSideboardLine(text: Component) : SideboardLine {
    override val textFlow = flowOf(text)
}

/**
 * A sideboard line which does change. Everytime the given [textFlow] emits
 * a new [Text], the sideboard line will be updated for all players currently
 * seeing the [Sideboard].
 */
@Deprecated(
    message = "Use SideboardLine.Changing instead.",
    replaceWith = ReplaceWith("SideboardLine.Changing")
)
class ChangingSideboardLine(override val textFlow: Flow<Component>) : SideboardLine
