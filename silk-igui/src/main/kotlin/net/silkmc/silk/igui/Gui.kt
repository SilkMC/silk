package net.silkmc.silk.igui

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.silkmc.silk.core.task.mcCoroutineScope
import net.silkmc.silk.igui.mixin.SimpleContainerAccessor

/**
 * Opens the given gui.
 *
 * @param pageKey (optional) specifies the key of the page which should be loaded
 * with the process of opening the gui
 *
 * @return the job of opening the gui and displaying it to the player
 */
fun ServerPlayer.openGui(gui: Gui, pageKey: Any? = null): Job {
    return mcCoroutineScope.launch {
        if (pageKey != null)
            gui.pagesByKey[pageKey.toString()]?.let { gui.loadPage(it) }

        openMenu(gui)
    }
}

class Gui(
    val guiType: GuiType,
    val title: Component,
    val pagesByKey: Map<String, GuiPage>,
    val pagesByNumber: Map<Int, GuiPage>,
    val defaultPageKey: String,
    val eventHandler: GuiEventHandler,
) : SimpleContainer(guiType.dimensions.slotAmount), MenuProvider {
    val views = HashMap<Player, GuiScreenHandler>()

    var isOffset = false
        private set

    var currentPage = pagesByKey[defaultPageKey] ?: error("The specified defaultPage does not exits")

    init {
        //loadPage(currentPage)
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    val accessor = this as SimpleContainerAccessor

    /**
     * Loads the specified page with the specified offset.
     */
    suspend fun loadPage(
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
                .forEach { accessor.items[it] = ItemStack.EMPTY }
        } else accessor.items.clear()

        page.content.forEach { (slotIndex, element) ->
            if (isOffset) {
                val guiSlot = guiType.dimensions.slotMap[slotIndex]
                if (guiSlot != null) {
                    val offsetIndex =
                        GuiSlot(guiSlot.row + offsetVertically, guiSlot.slotInRow + offsetHorizontally)
                            .slotIndexIn(guiType.dimensions)
                    if (offsetIndex != null)
                        accessor.items[offsetIndex] = element.getItemStack(offsetIndex)
                }
            } else accessor.items[slotIndex] = element.getItemStack(slotIndex)
        }

        setChanged()
    }

    /**
     * Reloads the current page.
     *
     * You probably do not need this function, as there should always be another (better)
     * way of updating the gui. This function is used internally.
     */
    suspend fun reloadCurrentPage() {
        if (!isOffset)
            loadPage(currentPage)
    }

    override fun createMenu(syncId: Int, inventory: Inventory, player: Player) =
        guiType.createScreenHandler(this, syncId, inventory, this).apply {
            if (views.isEmpty())
                currentPage.startUsing(this@Gui)
            views[player] = this
        }

    override fun getDisplayName() = title

    override fun stopOpen(player: Player) {
        super.stopOpen(player)

        views -= player
        if (views.isEmpty())
            currentPage.stopUsing(this)
    }

    /**
     * Closes this gui for all players viewing it.
     */
    fun closeForViewers() {
        views.keys.forEach {
            (it as ServerPlayer).closeContainer()
        }
    }
}
