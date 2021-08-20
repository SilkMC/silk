package net.axay.fabrik.igui

import net.axay.fabrik.igui.mixin.SimpleInventoryAccessor
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import java.util.*

/**
 * Opens the given gui.
 *
 * @param pageKey (optional) specifies the key of the page which should be loaded
 * with the process of opening the gui
 *
 * @return an [OptionalInt] which may contain the syncId of the inventory holding the gui
 */
fun PlayerEntity.openGui(gui: Gui, pageKey: Any? = null): OptionalInt {
    if (pageKey != null)
        gui.pagesByKey[pageKey.toString()]?.let { gui.loadPage(it) }

    return openHandledScreen(gui)
}

class Gui(
    val guiType: GuiType,
    val title: LiteralText,
    val pagesByKey: Map<String, GuiPage>,
    val pagesByNumber: Map<Int, GuiPage>,
    val defaultPageKey: String,
    val eventHandler: GuiEventHandler,
) : SimpleInventory(guiType.dimensions.slotAmount), NamedScreenHandlerFactory {
    val views = HashMap<PlayerEntity, GuiScreenHandler>()

    var isOffset = false
        private set

    var currentPage = pagesByKey[defaultPageKey] ?: error("The specified defaultPage does not exits")

    init {
        //loadPage(currentPage)
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    val accessor = this as SimpleInventoryAccessor

    /**
     * Loads the specified page with the specified offset.
     */
    fun loadPage(
        page: GuiPage,
        offsetHorizontally: Int = 0, offsetVertically: Int = 0,
    ) {
        if (
            (offsetHorizontally < guiType.dimensions.width / 2) &&
            (offsetVertically < guiType.dimensions.height / 2)
        ) {
            if (currentPage != page) {
                currentPage.stopUsing(this)
                page.startUsing(this)

                currentPage = page
            } else if (!page.inUse) {
                page.startUsing(this)
            }
        }

        isOffset = offsetHorizontally != 0 || offsetVertically != 0

        if (isOffset) {
            guiType.dimensions.guiSlots
                .mapNotNull {
                    GuiSlot(it.row + offsetVertically, it.slotInRow + offsetHorizontally)
                        .slotIndexIn(guiType.dimensions)
                }
                .forEach { accessor.stacks[it] = ItemStack.EMPTY }
        } else accessor.stacks.clear()

        page.content.forEach { (slotIndex, element) ->
            if (isOffset) {
                val guiSlot = guiType.dimensions.slotMap[slotIndex]
                if (guiSlot != null) {
                    val offsetIndex =
                        GuiSlot(guiSlot.row + offsetVertically, guiSlot.slotInRow + offsetHorizontally)
                            .slotIndexIn(guiType.dimensions)
                    if (offsetIndex != null)
                        accessor.stacks[offsetIndex] = element.getItemStack(offsetIndex)
                }
            } else accessor.stacks[slotIndex] = element.getItemStack(slotIndex)
        }

        markDirty()
    }

    /**
     * Reloads the current page.
     *
     * You probably do not need this function, as there should always be another (better)
     * way of updating the gui. This function is used internally.
     */
    fun reloadCurrentPage() {
        if (!isOffset)
            loadPage(currentPage)
    }

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity) =
        guiType.createScreenHandler(this, syncId, playerInv, this).apply {
            if (views.isEmpty())
                currentPage.startUsing(this@Gui)
            views[player] = this
        }

    override fun getDisplayName() = title

    override fun onClose(player: PlayerEntity) {
        views -= player
        if (views.isEmpty())
            currentPage.stopUsing(this)
    }

    /**
     * Closes this gui for all players viewing it.
     */
    fun closeForViewers() {
        views.keys.forEach {
            (it as ServerPlayerEntity).closeHandledScreen()
        }
    }
}
