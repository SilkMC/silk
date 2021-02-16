@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package net.axay.fabrik.igui

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class IGUIType<in T : ForInventory>(
    val dimensions: InventoryDimensions,
    val bukkitType: InventoryType? = null
) {

    companion object {

        val ONE_BY_NINE = IGUIType<ForInventoryOneByNine>(InventoryDimensions(9, 1))
        val TWO_BY_NINE = IGUIType<ForInventoryTwoByNine>(InventoryDimensions(9, 2))
        val THREE_BY_NINE = IGUIType<ForInventoryThreeByNine>(InventoryDimensions(9, 3))
        val FOUR_BY_NINE = IGUIType<ForInventoryFourByNine>(InventoryDimensions(9, 4))
        val FIVE_BY_NINE = IGUIType<ForInventoryFiveByNine>(InventoryDimensions(9, 5))
        val SIX_BY_NINE = IGUIType<ForInventorySixByNine>(InventoryDimensions(9, 6))
        val ONE_BY_FIVE =
            IGUIType<ForInventoryOneByFive>(InventoryDimensions(5, 1), bukkitType = InventoryType.HOPPER)
        val THREE_BY_THREE =
            IGUIType<ForInventoryThreeByThree>(InventoryDimensions(3, 3), bukkitType = InventoryType.DROPPER)

    }

    fun createBukkitInv(holder: InventoryHolder? = null, title: String? = null): Inventory {
        val realTitle = title ?: ""
        return when {
            bukkitType != null -> Bukkit.createInventory(holder, bukkitType, realTitle)
            else -> Bukkit.createInventory(holder, dimensions.slotAmount, realTitle)
        }
    }

}

// INVENTORY TYPE SAFETY

interface ForInventory

interface ForInventoryThreeByThree : ForInventoryThreeByNine
interface ForInventoryOneByFive : ForInventoryOneByNine
interface ForInventoryOneByNine : ForInventoryTwoByNine
interface ForInventoryTwoByNine : ForInventoryThreeByNine
interface ForInventoryThreeByNine : ForInventoryFourByNine
interface ForInventoryFourByNine : ForInventoryFiveByNine
interface ForInventoryFiveByNine : ForInventorySixByNine
interface ForInventorySixByNine : ForInventory

interface ForEveryInventory
    : ForInventoryOneByNine, ForInventoryTwoByNine, ForInventoryThreeByNine,
    ForInventoryFourByNine, ForInventoryFiveByNine, ForInventorySixByNine,
    ForInventoryThreeByThree, ForInventoryOneByFive

interface ForInventoryWidthThree : ForInventoryThreeByThree
interface ForInventoryWidthFive : ForInventoryOneByFive
interface ForInventoryWidthNine : ForInventoryOneByNine, ForInventoryTwoByNine, ForInventoryThreeByNine,
    ForInventoryFourByNine, ForInventoryFiveByNine, ForInventorySixByNine
