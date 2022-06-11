package net.axay.silk.test.commands

import net.axay.silk.core.item.*
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.enchantment.Enchantments

private fun createItems(player: ServerPlayer) = listOf(
    itemStack(Items.NETHERITE_SWORD) {
        enchant(Enchantments.SHARPNESS, 2)
    },
    itemStack(Items.POTION) {
        setPotion(Potions.LEAPING)
    },
    itemStack(Items.PLAYER_HEAD) {
        setCustomName("Llama")
        setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg5YTJlYjE3NzA1ZmU3MTU0YWIwNDFlNWM3NmEwOGQ0MTU0NmEzMWJhMjBlYTMwNjBlM2VjOGVkYzEwNDEyYyJ9fX0=")
    },
    itemStack(Items.PLAYER_HEAD) {
        setCustomName("Green Apple")
        setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRlMTQzYThjNTdjNTUwYjg0MDAzNTI3MzA4N2QxZDliMjM2Yjk1NDEwZDg3MTMxZmQ1ZDJmN2YwNzY4ZDEyNiJ9fX0=")
    },
    itemStack(Items.PLAYER_HEAD) {
        setSkullPlayer(player)
    },
)

val itemTestCommand = testCommand("item") {
    runs {
        val player = source.playerOrException
        val inventory = player.inventory
        createItems(player).forEach { stack ->
            inventory.add(stack.copy())
        }
    }
}
