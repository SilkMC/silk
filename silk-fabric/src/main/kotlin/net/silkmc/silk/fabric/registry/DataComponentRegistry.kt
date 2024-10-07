@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.component.DataComponentType
import kotlin.experimental.ExperimentalTypeInference


@OptIn(ExperimentalTypeInference::class)
fun <T> dataComponentTypeOf(@BuilderInference builder: DataComponentType.Builder<T>.() -> Unit): DataComponentType<T> {
    val dataComponentTypeBuilder = DataComponentType.builder<T>()

    return dataComponentTypeBuilder.apply(builder).build()
}
