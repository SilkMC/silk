package net.axay.fabrik.igui.observable

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.axay.fabrik.core.task.mcSyncLaunch

/**
 * Parent class of the immutable [GuiList] and mutable [GuiMutableList].
 */
abstract class AbstractGuiList<T, out L : List<T>>(@PublishedApi internal val collection: L) {
    protected val onChangeListeners = HashSet<suspend (List<T>) -> Unit>()

    /**
     * Registers an onChangeListener, which will be called if the content of this list is mutated.
     */
    open fun onChange(block: suspend (List<T>) -> Unit) = Unit

    /**
     * Unregisters an already registered onChangeListener.
     */
    open fun removeOnChangeListener(block: suspend (List<T>) -> Unit) = Unit
}

/**
 * Wraps a normal list into a format which can be used by specific gui components - the most
 * prominent one being [net.axay.fabrik.igui.GuiCompound]. This gui list is immutable.
 */
class GuiList<T>(collection: List<T>) : AbstractGuiList<T, List<T>>(collection)

/**
 * Does the same as [GuiList], but is mutatable. If you change the content if this list using the
 * [mutate] function, the gui will update accordingly.
 *
 * @see GuiList
 */
class GuiMutableList<T>(collection: MutableList<T>) : AbstractGuiList<T, MutableList<T>>(collection) {
    override fun onChange(block: suspend (List<T>) -> Unit) {
        onChangeListeners += block
    }

    override fun removeOnChangeListener(block: suspend (List<T>) -> Unit) {
        onChangeListeners -= block
    }

    @PublishedApi
    internal suspend fun invokeListeners() = onChangeListeners.forEach { it.invoke(collection) }

    @PublishedApi
    internal val mutateMutex = Mutex()

    /**
     * Inside the given [block], you can mutate this list. All guis currently using this list will be
     * informed after that mutation, to update properly.
     */
    suspend inline fun mutate(crossinline block: suspend (MutableList<T>) -> Unit) = coroutineScope {
        launch {
            mutateMutex.withLock {
                block(collection)
            }
            mcSyncLaunch {
                invokeListeners()
            }
        }
    }
}

/**
 * Wraps this list in a [GuiList]. This list is immutable and therefore static in the gui.
 */
fun <T> List<T>.asGuiList() = GuiList(this)

/**
 * Wraps this list in a [GuiMutableList], which informs gui components about any content updates.
 */
fun <T> MutableList<T>.asMutableGuiList() = GuiMutableList(this)

/**
 * Creates a new [GuiList] out of the content of this iterable. This list is immutable and therefore static in the gui.
 */
fun <T> Iterable<T>.toGuiList() = GuiList(toList())

/**
 * Creates a new [GuiMutableList], which informs gui components about any content updates,
 * out of the content of this iterable.
 */
fun <T> Iterable<T>.toMutableGuiList() = GuiMutableList(toMutableList())
