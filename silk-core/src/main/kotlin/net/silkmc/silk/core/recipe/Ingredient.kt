@file:Suppress("unused")

package net.silkmc.silk.core.recipe

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

fun ingredientOf(vararg items: ItemLike): Ingredient {
    return Ingredient.of(*items)
}

fun ingredientOf(vararg items: ItemStack): Ingredient {
    return Ingredient.of(*items)
}

fun ingredientOf(items: TagKey<Item>): Ingredient {
    return Ingredient.of(items)
}
