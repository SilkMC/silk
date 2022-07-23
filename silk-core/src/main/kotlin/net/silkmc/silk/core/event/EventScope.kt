package net.silkmc.silk.core.event

import net.silkmc.silk.core.annotations.ExperimentalSilkApi

/**
 * An event scope can provide additional information and / or
 * functions to event handlers without the event itself having
 * to implement these features.
 * The prime example for this is cancellation, see [Cancellable].
 */
@ExperimentalSilkApi
data class EventScope<E, R>(val event: E, val result: R)
