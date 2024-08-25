@file:Suppress("RedundantSamConstructor")

package net.silkmc.silk.paper.events

import net.silkmc.silk.core.Silk
import net.silkmc.silk.paper.internal.SilkPaperEntrypoint
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin

/**
 * Registers an event listener for the specified event type [T],
 * invoking the specified [callback] when the event is called.
 *
 * @param plugin The plugin to register the listener for.
 * @param priority The priority of the event listener,
 * defining the order in which listeners are called.
 */
inline fun <reified T : Event> listen(
    plugin: Plugin,
    priority: EventPriority = EventPriority.NORMAL,
    crossinline callback: (T) -> Unit,
) {
    val server = Silk.server
        ?: throw IllegalStateException("You cannot register events while the server instance is not available.")

    server.server.pluginManager.registerEvent(
        T::class.java,
        DummyListener,
        priority,
        EventExecutor { _, event ->
            callback(event as T)
        },
        plugin,
    )
}

/**
 * **Internal** function for Silk to register its own event listeners.
 * Registers an event listener for the specified event type [T],
 * invoking the specified [callback] when the event is called.
 *
 * @param priority The priority of the event listener,
 * defining the order in which listeners are called.
 */
internal inline fun <reified T : Event> listenSilk(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline callback: (T) -> Unit,
) {
    val server = Silk.server
        ?: throw IllegalStateException("You cannot register events while the server instance is not available.")

    server.server.pluginManager.registerEvent(
        T::class.java,
        DummyListener,
        priority,
        EventExecutor { _, event ->
            callback(event as T)
        },
        SilkPaperEntrypoint.instance,
    )
}

/**
 * A dummy listener to register events with.
 * This listener is not actually used, but is required to register events.
 */
@PublishedApi
internal object DummyListener : Listener
