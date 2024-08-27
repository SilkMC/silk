package net.silkmc.silk.core.registry

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import kotlin.experimental.ExperimentalTypeInference


@OptIn(ExperimentalTypeInference::class)
fun <T> dataComponentTypeOf(@BuilderInference builder: DataComponentType.Builder<T>.() -> Unit): DataComponentType<T> {
    val dataComponentTypeBuilder = DataComponentType.builder<T>()

    return dataComponentTypeBuilder.apply(builder).build()
}
