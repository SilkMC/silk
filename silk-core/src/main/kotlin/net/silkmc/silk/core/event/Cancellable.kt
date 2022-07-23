package net.silkmc.silk.core.event

import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@ExperimentalSilkApi
sealed interface Cancellable {
    @ExperimentalSilkApi
    object Active : Cancellable

    @ExperimentalSilkApi
    object Cancelled : Cancellable
}
