package net.silkmc.silk.core.event

/**
 * Enum constants representing a priority which an event listener may
 * have over other event listeners.
 * The priority goes from [FIRST] (executed before everything else) to
 * [LAST] (executed after everything else has finished).
 */
enum class EventPriority {
    FIRST,
    EARLY,
    NORMAL,
    LATE,
    LAST;
}
