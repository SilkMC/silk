@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.Holder

fun <T : Any> T.asHolder(): Holder<T> = Holder.direct(this)

val <T> Holder<T>.value: T
    get() = value()

