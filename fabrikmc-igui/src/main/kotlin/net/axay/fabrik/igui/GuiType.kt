@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ScreenHandlerType

enum class GuiType(
    val screenHandlerType: ScreenHandlerType<*>,
    val dimensions: GuiDimensions,
) {
    NINE_BY_ONE(ScreenHandlerType.GENERIC_9X1, GuiDimensions(9, 1)),
    NINE_BY_TWO(ScreenHandlerType.GENERIC_9X2, GuiDimensions(9, 2)),
    NINE_BY_THREE(ScreenHandlerType.GENERIC_9X3, GuiDimensions(9, 3)),
    NINE_BY_FOUR(ScreenHandlerType.GENERIC_9X4, GuiDimensions(9, 4)),
    NINE_BY_FIVE(ScreenHandlerType.GENERIC_9X5, GuiDimensions(9, 5)),
    NINE_BY_SIX(ScreenHandlerType.GENERIC_9X6, GuiDimensions(9, 6)),

    THREE_BY_THREE(ScreenHandlerType.GENERIC_3X3, GuiDimensions(3, 3)),
    ;

    fun createScreenHandler(gui: Gui, syncId: Int, playerInv: PlayerInventory, inv: Inventory) =
        GuiScreenHandler(gui, syncId, playerInv, inv)
}
