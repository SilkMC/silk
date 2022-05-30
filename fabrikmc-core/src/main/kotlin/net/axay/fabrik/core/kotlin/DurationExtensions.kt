package net.axay.fabrik.core.kotlin

import kotlin.time.Duration.Companion.milliseconds

/**
 * Returns a [kotlin.time.Duration] for the time the given amount
 * of ticks would take *in an ideal scenario*.
 */
val Int.ticks get() = 50.milliseconds.times(this)
