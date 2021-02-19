@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import java.util.*
import kotlin.collections.HashSet

class GuiData(
    val guiType: GuiType,
    val title: String?,
    internal val pages: Map<Int, GuiPage>,
    val defaultPage: Int,
    val transitionTo: InventoryChangeEffect?,
    val transitionFrom: InventoryChangeEffect?,
    internal val generalOnClick: ((GuiClickEvent) -> Unit)?
)

abstract class Gui(
    val data: GuiData
) {

    /**
     * Returns the instance beloning to the given player.
     * If not existing, a new instance will be created.
     */
    abstract fun getInstance(player: PlayerEntity): GuiInstance

    /**
     * Returns all active instances of this gui.
     */
    abstract fun getAllInstances(): Collection<GuiInstance>

    /**
     * Closes this Gui for all viewers and unregisters
     * all instances.
     */
    abstract fun closeGui()

    protected fun unregisterAndClose() {
        getAllInstances().forEach {
            it.inventory.closeForViewers()
            it.unregister()
        }
    }

}

class GuiShared(
    guiData: GuiData
) : Gui(guiData) {

    private var _singleInstance: GuiInstance? = null
    val singleInstance
        get() = _singleInstance ?: GuiInstance(this, null).apply {
            _singleInstance = this
        }

    override fun getInstance(player: PlayerEntity) = singleInstance

    override fun getAllInstances() = _singleInstance?.let { listOf(it) } ?: emptyList()

    override fun closeGui() {
        unregisterAndClose()
        _singleInstance = null
    }

}

class GuiIndividual(
    guiData: GuiData
) : Gui(guiData) {

    private val playerInstances = HashMap<PlayerEntity, GuiInstance>()

    override fun getInstance(player: PlayerEntity) =
        playerInstances[player] ?: createInstance(player)

    override fun getAllInstances() = playerInstances.values

    private fun createInstance(player: PlayerEntity) =
        GuiInstance(this, player).apply {
            playerInstances[player] = this
        }

    fun deleteInstance(player: PlayerEntity) = playerInstances.remove(player)?.unregister()

    override fun closeGui() {
        unregisterAndClose()
        playerInstances.clear()
    }

}

class GuiInstance(
    val gui: Gui,
    holder: PlayerEntity?
) {

    internal val inventory = GuiInventory(this)

    private val currentElements = HashSet<GuiElement>()

    internal var isInMove: Boolean = false

    var currentPageInt: Int = gui.data.defaultPage; private set
    val currentPage
        get() = getPage(currentPageInt)
            ?: throw IllegalStateException("The currentPageInt has no associated page!")

    init {
        loadPageUnsafe(gui.data.defaultPage)
    }

    internal fun loadPageUnsafe(page: Int, offsetHorizontally: Int = 0, offsetVertically: Int = 0) {
        gui.data.pages[page]?.let { loadPageUnsafe(it, offsetHorizontally, offsetVertically) }
    }

    internal fun loadPageUnsafe(page: GuiPage, offsetHorizontally: Int = 0, offsetVertically: Int = 0) {

        val ifOffset = offsetHorizontally != 0 || offsetVertically != 0

        if (!ifOffset) {

            // unregister this inv from all elements on the previous page
            currentElements.forEach { it.stopUsing(this) }
            currentElements.clear()

            // register this inv for all new elements
            HashSet(page.slots.values).forEach {
                if (it is GuiElement) {
                    currentElements += it
                    it.startUsing(this)
                }
            }

            currentPageInt = page.number

        }

        loadContent(page.slots, offsetHorizontally, offsetVertically)

    }

    internal fun loadContent(
        content: Map<Int, GuiSlot>,
        offsetHorizontally: Int = 0,
        offsetVertically: Int = 0
    ) {

        val ifOffset = offsetHorizontally != 0 || offsetVertically != 0

        val dimensions = gui.data.guiType.dimensions

        // clear the space which will be redefined
        if (ifOffset) {
            dimensions.invSlots.forEach {
                val slotToClear = dimensions.invSlotsWithRealSlots[it.add(offsetHorizontally, offsetVertically)]
                if (slotToClear != null) inventory.setStack(slotToClear, ItemStack(Items.AIR))
            }
        } else inventory.clear()

        // render the given content
        content.forEach {

            val slot = it.value
            if (slot is GuiElement) {

                if (ifOffset) {
                    val invSlot = InventorySlot.fromRealSlot(it.key, dimensions)
                    if (invSlot != null) {
                        val offsetSlot = invSlot.add(offsetHorizontally, offsetVertically).realSlotIn(dimensions)
                        if (offsetSlot != null) inventory.setStack(offsetSlot, slot.getItemStack(offsetSlot))
                    }
                } else inventory.setStack(it.key, slot.getItemStack(it.key))

            }

        }

    }

    /**
     * Stops KSpigot from listening to actions in this
     * Gui anymore.
     */
    fun unregister() {
        // unregister this inv from all elements
        currentElements.forEach { it.stopUsing(this) }
        currentElements.clear()
    }

    /**
     * @return True, if the [inventory] belongs to this Gui.
     */
    fun isThisInv(inventory: GuiInventory) = inventory == this.inventory

    /**
     * Loads the specified page in order to display it in the Gui.
     */
    fun loadPage(page: GuiPage) = loadPageUnsafe(page)

    /**
     * Temporarily sets the given item at the given slots.
     */
    operator fun set(slot: InventorySlotCompound, value: ItemStack) {
        slot.realSlotsWithInvType(gui.data.guiType).forEach {
            inventory.setStack(it, value)
        }
    }

    /**
     * Searches for a page associated to the given [page] index.
     */
    fun getPage(page: Int?) = gui.data.pages[page]

    /**
     * Reloads the current page.
     */
    fun reloadCurrentPage() {
        if (!isInMove)
            loadPage(currentPage)
    }

}
