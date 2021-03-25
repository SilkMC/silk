package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import java.util.*

/**
 * Opens the given gui.
 *
 * @param page (optional) specifies the key of the page which should be loaded
 * with the process of opening the gui
 *
 * @return an [OptionalInt] which may contain the syncId of the inventory holding the gui
 */
fun PlayerEntity.openGui(gui: Gui, page: String? = null): OptionalInt {
    if (page != null)
        gui.pagesByKey[page]?.let { gui.loadPage(it) }

    return openHandledScreen(gui.inventory)
}

class Gui(
    val guiType: GuiType,
    val title: String,
    val pagesByKey: Map<String, GuiPage>,
    val pagesByNumber: Map<Int, GuiPage>,
    val defaultPageKey: String,
    val eventHandler: GuiEventHandler,
) {
    val inventory = GuiInventory(this)

    var isOffset = false
        private set

    var currentPage = pagesByKey[defaultPageKey] ?: error("The specified defaultPage does not exits")
    init {
        loadPage(currentPage)
    }

    fun loadPage(
        page: GuiPage,
        offsetHorizontally: Int = 0, offsetVertically: Int = 0,
    ) {
        if (
            (offsetHorizontally < guiType.dimensions.width / 2) &&
            (offsetVertically < guiType.dimensions.height / 2)
        ) currentPage = page

        isOffset = offsetHorizontally != 0 || offsetVertically != 0

        if (isOffset) {
            guiType.dimensions.guiSlots
                .mapNotNull {
                    GuiSlot(it.row + offsetVertically, it.slotInRow + offsetHorizontally)
                        .slotIndexIn(guiType.dimensions)
                }
                .forEach { inventory.setStack(it, ItemStack.EMPTY) }
        } else inventory.clear()

        page.content.forEach { (slotIndex, element) ->
            if (isOffset) {
                val guiSlot = guiType.dimensions.slotMap[slotIndex]
                if (guiSlot != null) {
                    inventory.setStack(
                        GuiSlot(guiSlot.row + offsetVertically, guiSlot.slotInRow + offsetHorizontally)
                            .slotIndexIn(guiType.dimensions)!!,
                        element.itemStack
                    )
                }
            } else inventory.setStack(slotIndex, element.itemStack)
        }
    }
}
