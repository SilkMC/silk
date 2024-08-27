package net.silkmc.silk.core.registry

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient


fun armorMaterialOf(
    defensePoints: Map<ArmorItem.Type, Int>,
    enchantability: Int,
    equipSound: Holder<SoundEvent>,
    repairIngredientSupplier: () -> Ingredient,
    toughness: Float,
    knockbackResistance: Float,
    layerId: String,
    dyeable: Boolean = false,
    layerSuffix: String = "",
): Holder<ArmorMaterial>? {
    // Get the supported layers for the armor material
    val layers = listOf(
        ArmorMaterial.Layer(ResourceLocation.parse(layerId), layerSuffix, dyeable)
    )
    return armorMaterialOf(defensePoints, enchantability, equipSound, repairIngredientSupplier, toughness, knockbackResistance, layers)
}

fun armorMaterialOf(
    defensePoints: Map<ArmorItem.Type, Int>,
    enchantability: Int,
    equipSound: Holder<SoundEvent>,
    repairIngredientSupplier: () -> Ingredient,
    toughness: Float,
    knockbackResistance: Float,
    layers: List<ArmorMaterial.Layer>,
): Holder<ArmorMaterial>? {
    return Holder.direct(
        ArmorMaterial(
            defensePoints,
            enchantability,
            equipSound,
            repairIngredientSupplier,
            layers,
            toughness,
            knockbackResistance,
        )
    )
}

fun ArmorMaterial.register(id: ResourceLocation): Holder.Reference<ArmorMaterial> {
    return BuiltInRegistries.ARMOR_MATERIAL.registerForHolder(id, this)
}

fun ArmorMaterial.register(id: String): Holder.Reference<ArmorMaterial> {
    return BuiltInRegistries.ARMOR_MATERIAL.registerForHolder(id, this)
}

fun Holder<ArmorMaterial>.register(id: ResourceLocation): Holder<ArmorMaterial> {
    return BuiltInRegistries.ARMOR_MATERIAL.registerForHolder(id, this.value)
}

fun Holder<ArmorMaterial>.register(id: String): Holder<ArmorMaterial> {
    return BuiltInRegistries.ARMOR_MATERIAL.registerForHolder(id, this.value)
}
