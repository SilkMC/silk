@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ScreenHandlerType

enum class GuiType(
    val screenHandlerType: ScreenHandlerType<*>,
    val dimensions: InventoryDimensions,
) {
    NINE_BY_ONE(ScreenHandlerType.GENERIC_9X1, InventoryDimensions(9, 1)),
    NINE_BY_TWO(ScreenHandlerType.GENERIC_9X2, InventoryDimensions(9, 2)),
    NINE_BY_THREE(ScreenHandlerType.GENERIC_9X3, InventoryDimensions(9, 3)),
    NINE_BY_FOUR(ScreenHandlerType.GENERIC_9X4, InventoryDimensions(9, 4)),
    NINE_BY_FIVE(ScreenHandlerType.GENERIC_9X5, InventoryDimensions(9, 5)),
    NINE_BY_SIX(ScreenHandlerType.GENERIC_9X6, InventoryDimensions(9, 6)),

    THREE_BY_SIX(ScreenHandlerType.GENERIC_3X3, InventoryDimensions(3, 3)),
    ;

    fun createScreenHandler(guiInstance: GuiInstance, syncId: Int, playerInv: PlayerInventory, inv: Inventory) =
        GuiScreenHandler(guiInstance, syncId, playerInv, inv)
}
