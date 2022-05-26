package net.axay.fabrik.test.commands

import net.axay.fabrik.core.item.itemStack
import net.axay.fabrik.core.item.setPotion
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.enchantment.Enchantments

private val items = listOf(
    itemStack(Items.NETHERITE_SWORD) {
        enchant(Enchantments.SHARPNESS, 2)
    },
    itemStack(Items.POTION) {
        setPotion(Potions.LEAPING)
    }
)

val itemTestCommand = testCommand("item") {
    runs {
        with(source.playerOrException.inventory) { items.forEach(::add) }
    }
}
