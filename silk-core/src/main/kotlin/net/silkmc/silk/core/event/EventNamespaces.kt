package net.silkmc.silk.core.event

import net.silkmc.silk.core.annotations.ExperimentalSilkApi

@Suppress("UnusedReceiverParameter") // receiver is for namespacing only
val Events.Entity get() = EntityEvents

@ExperimentalSilkApi
object EntityEvents
