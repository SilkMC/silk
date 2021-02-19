@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import java.util.*
import kotlin.collections.HashSet

class GUIData(
    val guiType: GUIType,
    val title: String?,
    internal val pages: Map<Int, GUIPage>,
    val defaultPage: Int,
    val transitionTo: InventoryChangeEffect?,
    val transitionFrom: InventoryChangeEffect?,
    internal val generalOnClick: ((GUIClickEvent) -> Unit)?
)

abstract class GUI(
    val data: GUIData
) {

    /**
     * Returns the instance beloning to the given player.
     * If not existing, a new instance will be created.
     */
    abstract fun getInstance(player: PlayerEntity): GUIInstance

    /**
     * Returns all active instances of this GUI.
     */
    abstract fun getAllInstances(): Collection<GUIInstance>

    /**
     * Closes this GUI for all viewers and unregisters
     * all instances.
     */
    abstract fun closeGUI()

    protected fun unregisterAndClose() {
        getAllInstances().forEach {
            it.inventory.closeForViewers()
            it.unregister()
        }
    }

}

class GUIShared(
    guiData: GUIData
) : GUI(guiData) {

    private var _singleInstance: GUIInstance? = null
    val singleInstance
        get() = _singleInstance ?: GUIInstance(this, null).apply {
            _singleInstance = this
            register()
        }

    override fun getInstance(player: PlayerEntity) = singleInstance

    override fun getAllInstances() = _singleInstance?.let { listOf(it) } ?: emptyList()

    override fun closeGUI() {
        unregisterAndClose()
        _singleInstance = null
    }

}

class GUIIndividual(
    guiData: GUIData,
    resetOnClose: Boolean,
    resetOnQuit: Boolean
) : GUI(guiData) {

    private val playerInstances = HashMap<PlayerEntity, GUIInstance>()

    override fun getInstance(player: PlayerEntity) =
        playerInstances[player] ?: createInstance(player)

    override fun getAllInstances() = playerInstances.values

    private fun createInstance(player: PlayerEntity) =
        GUIInstance(this, player).apply {
            playerInstances[player] = this
            register()
        }

    fun deleteInstance(player: PlayerEntity) = playerInstances.remove(player)?.unregister()

    override fun closeGUI() {
        unregisterAndClose()
        playerInstances.clear()
    }

    init {

        if (resetOnClose) {
            listen<InventoryCloseEvent> {
                deleteInstance(it.player as? Player ?: return@listen)
            }
        }

        if (resetOnQuit) {
            listen<PlayerQuitEvent> {
                deleteInstance(it.player)
            }
        }

    }

}

class GUIInstance(
    val gui: GUI,
    holder: PlayerEntity?
) {

    internal val inventory = GUIInventory(this)

    private val currentElements = HashSet<GUIElement>()

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

    internal fun loadPageUnsafe(page: GUIPage, offsetHorizontally: Int = 0, offsetVertically: Int = 0) {

        val ifOffset = offsetHorizontally != 0 || offsetVertically != 0

        if (!ifOffset) {

            // unregister this inv from all elements on the previous page
            currentElements.forEach { it.stopUsing(this) }
            currentElements.clear()

            // register this inv for all new elements
            HashSet(page.slots.values).forEach {
                if (it is GUIElement) {
                    currentElements += it
                    it.startUsing(this)
                }
            }

            currentPageInt = page.number

        }

        loadContent(page.slots, offsetHorizontally, offsetVertically)

    }

    internal fun loadContent(
        content: Map<Int, GUISlot>,
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
            if (slot is GUIElement) {

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
     * Registers this GUI.
     * (KSpigot will listen for actions in the inventory.)
     */
    @Suppress("UNCHECKED_CAST")
    fun register() = GUIHolder.register(this)

    /**
     * Stops KSpigot from listening to actions in this
     * GUI anymore.
     */
    fun unregister() {

        @Suppress("UNCHECKED_CAST")
        (GUIHolder.unregister(this))

        // unregister this inv from all elements
        currentElements.forEach { it.stopUsing(this) }
        currentElements.clear()

    }

    /**
     * @return True, if the [inventory] belongs to this GUI.
     */
    fun isThisInv(inventory: GUIInventory) = inventory == this.inventory

    /**
     * Loads the specified page in order to display it in the GUI.
     */
    fun loadPage(page: GUIPage) = loadPageUnsafe(page)

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
