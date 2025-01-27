@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset
import net.minecraft.world.item.equipment.EquipmentAssets

fun armorMaterialOf(
    baseDurability: Int,
    defensePoints: Map<ArmorType, Int>,
    enchantability: Int,
    equipSound: Holder<SoundEvent>,
    toughness: Float,
    knockbackResistance: Float,
    repairIngredient: TagKey<Item>,
    layers: ResourceKey<EquipmentAsset>,
): ArmorMaterial {
    return ArmorMaterial(
        baseDurability,
        defensePoints,
        enchantability,
        equipSound,
        toughness,
        knockbackResistance,
        repairIngredient,
        layers,
    )
}

fun armorMaterialKeyOf(id: ResourceLocation): ResourceKey<EquipmentAsset> = resourceKeyOf(EquipmentAssets.ROOT_ID, id)

fun armorMaterialKeyOf(id: String): ResourceKey<EquipmentAsset> = armorMaterialKeyOf(ResourceLocation.parse(id))

fun armorMaterialKeyOf(namespace: String, path: String): ResourceKey<EquipmentAsset> {
    return armorMaterialKeyOf(ResourceLocation.fromNamespaceAndPath(namespace, path))
}
