@file:Suppress("unused")

package net.silkmc.silk.core.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item


inline fun itemProperties(
    builder: Item.Properties.() -> Unit,
): Item.Properties = Item.Properties().apply(builder)

fun <T : Item> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.ITEM.register(id, this)
}

fun <T : Item> T.register(id: String): T {
    return register(ResourceLocation.parse(id))
}
