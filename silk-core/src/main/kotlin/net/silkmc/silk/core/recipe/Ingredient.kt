@file:Suppress("unused")

package net.silkmc.silk.core.recipe

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

fun ingredientOf(vararg items: ItemLike): Ingredient {
    return Ingredient.of(*items)
}

fun ingredientOf(items: List<ItemLike>): Ingredient {
    return Ingredient.of(items.stream())
}

fun ingredientOf(items: TagKey<Item>): Ingredient {
    return Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(items))
}
