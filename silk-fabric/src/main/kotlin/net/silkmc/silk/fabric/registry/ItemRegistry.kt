@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity


inline fun itemPropertiesOf(
    maxCount: Int? = null,
    maxDamage: Int? = null,
    rarity: Rarity? = null,
    builder: Item.Properties.() -> Unit = {},
): Item.Properties {
    val properties = Item.Properties()

    if (maxCount != null)
        properties.stacksTo(maxCount)

    if (maxDamage != null)
        properties.durability(maxDamage)

    if (rarity != null)
        properties.rarity(rarity)

    return properties.apply(builder)
}

inline fun foodPropertiesOf(
    nutrition: Int? = null,
    saturation: Float? = null,
    alwaysEdible: Boolean = false,
    builder: FoodProperties.Builder.() -> Unit = {},
): FoodProperties {
    val foodPropertiesBuilder = FoodProperties.Builder()

    if (nutrition != null)
        foodPropertiesBuilder.nutrition(nutrition)

    if (saturation != null)
        foodPropertiesBuilder.saturationModifier(saturation)

    if (alwaysEdible)
        foodPropertiesBuilder.alwaysEdible()

    return foodPropertiesBuilder.apply(builder).build()
}

fun <T : Item> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.ITEM.register(id, this)
}

fun <T : Item> T.register(id: String): T {
    return BuiltInRegistries.ITEM.register(id, this)
}
