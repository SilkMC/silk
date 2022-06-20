package net.silkmc.silk.igui.observable

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.silkmc.silk.core.task.mcCoroutineScope

/**
 * A property which can be used to represent mutable state in a gui. You can use
 * the [net.silkmc.silk.igui.guiIcon] function to specify how the value of this
 * property can be rendered to the gui.
 */
class GuiProperty<T>(private var value: T) {
    private val onChangeListeners = HashSet<suspend (T) -> Unit>()

    private val setValueMutex = Mutex()

    /**
     * Registers a new onChange listener, which will be called if the value of this property
     * is updated.
     */
    fun onChange(block: suspend (T) -> Unit) {
        onChangeListeners += block
    }

    /**
     * Unregisters an already registered onChangeListener.
     */
    fun removeOnChangeListener(block: suspend (T) -> Unit) {
        onChangeListeners -= block
    }

    /**
     * Invokes all listeners, causing all open guis using this list to update.
     */
    fun invokeListeners() = mcCoroutineScope.launch {
        onChangeListeners.forEach { it.invoke(value) }
    }

    /**
     * Gets the current value of the property. This function is thread-safe.
     */
    suspend fun get() = setValueMutex.withLock { value }

    /**
     * Sets the value of this property, this will cause all guis which are currently displaying
     * a [net.silkmc.silk.igui.GuiIcon] using this property to update. This function is thread-safe.
     *
     * @return the job updating the icons and guis
     */
    suspend fun set(value: T) = coroutineScope {
        setValueMutex.withLock {
            this@GuiProperty.value = value
        }
        invokeListeners()
    }
}
