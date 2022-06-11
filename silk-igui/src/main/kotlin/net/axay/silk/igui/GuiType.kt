@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package net.axay.silk.igui

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType

enum class GuiType(
    val screenHandlerType: MenuType<*>,
    val dimensions: GuiDimensions,
) {
    NINE_BY_ONE(MenuType.GENERIC_9x1, GuiDimensions(9, 1)),
    NINE_BY_TWO(MenuType.GENERIC_9x2, GuiDimensions(9, 2)),
    NINE_BY_THREE(MenuType.GENERIC_9x3, GuiDimensions(9, 3)),
    NINE_BY_FOUR(MenuType.GENERIC_9x4, GuiDimensions(9, 4)),
    NINE_BY_FIVE(MenuType.GENERIC_9x5, GuiDimensions(9, 5)),
    NINE_BY_SIX(MenuType.GENERIC_9x6, GuiDimensions(9, 6)),

    THREE_BY_THREE(MenuType.GENERIC_3x3, GuiDimensions(3, 3)),
    ;

    fun createScreenHandler(gui: Gui, syncId: Int, inventory: Inventory, container: Container) =
        GuiScreenHandler(gui, syncId, inventory, container)
}
