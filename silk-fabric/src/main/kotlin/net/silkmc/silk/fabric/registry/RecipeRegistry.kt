@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

fun <T : RecipeSerializer<*>> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.RECIPE_SERIALIZER.register(id, this)
}

fun <T : RecipeSerializer<*>> T.register(id: String): T {
    return BuiltInRegistries.RECIPE_SERIALIZER.register(id, this)
}


fun <T : RecipeType<*>> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.RECIPE_TYPE.register(id, this)
}

fun <T : RecipeType<*>> T.register(id: String): T {
    return BuiltInRegistries.RECIPE_TYPE.register(id, this)
}

