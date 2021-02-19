@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.*
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

class GuiSpaceCompoundElement<E> internal constructor(
    private val compound: AbstractGuiSpaceCompound<E>
) : GuiElement() {

    override fun getItemStack(slot: Int) = compound.getItemStack(slot)

    override fun shouldCancel(clickEvent: GuiClickEvent) = true // TODO

    override fun onClickElement(clickEvent: GuiClickEvent) {
        compound.onClickElement(clickEvent)
    }

    /*
     * The following two methods register and unregister the instance
     * for each compound element, but that is ok because it gets
     * added / removed to / from a HashSet
     */

    override fun startUsing(gui: GuiInstance) = compound.registerGui(gui)

    override fun stopUsing(gui: GuiInstance) = compound.unregisterGui(gui)

}

class GuiRectSpaceCompound<E>(
    invType: GuiType,
    iconGenerator: (E) -> ItemStack,
    onClick: ((GuiClickEvent, E) -> Unit)?,
    internal val compoundWidth: Int
) : AbstractGuiSpaceCompound<E>(invType, iconGenerator, onClick) {

    override fun handleScrollEndReached(newProgress: Int, internalSlotsSize: Int, contentSize: Int) =
        (internalSlotsSize + newProgress <= contentSize + (compoundWidth - (contentSize % compoundWidth)))

}

class GuiSpaceCompound<E>(
    invType: GuiType,
    iconGenerator: (E) -> ItemStack,
    onClick: ((GuiClickEvent, E) -> Unit)?
) : AbstractGuiSpaceCompound<E>(invType, iconGenerator, onClick) {

    override fun handleScrollEndReached(newProgress: Int, internalSlotsSize: Int, contentSize: Int) = false

}

abstract class AbstractGuiSpaceCompound<E> internal constructor(
    val guiType: GuiType,
    private val iconGenerator: (E) -> ItemStack,
    private val onClick: ((GuiClickEvent, E) -> Unit)?
) {

    private val content = ArrayList<E>()
    private var currentContent: List<E> = emptyList()

    private val internalSlots: MutableList<Int> = ArrayList()

    private var scrollProgress: Int = 0

    private var contentSort: () -> Unit = { }

    private val registeredGuis = HashSet<GuiInstance>()

    private fun contentAtSlot(slot: Int) = currentContent.getOrNull(internalSlots.indexOf(slot))

    private fun recalculateCurrentContent() {

        if (scrollProgress > content.size)
            throw IllegalStateException("The scrollProgress is greater than the content size.")

        // avoid IndexOutOfBoundsException
        var sliceUntil = internalSlots.size + scrollProgress
        if (sliceUntil > content.lastIndex)
            sliceUntil = content.size

        currentContent = content.slice(scrollProgress until sliceUntil)

    }

    private fun updateOpenGuis() {
        registeredGuis.forEach { it.reloadCurrentPage() }
    }

    internal fun scroll(distance: Int): Boolean {
        val value = scrollProgress + distance
        return if (value >= 0) {

            // always scroll if the end of the content is not reached
            val ifScroll = if (internalSlots.size + value <= content.size) true
            // scroll further if the width of the compound is defined and the last line can be filled up
            else handleScrollEndReached(value, internalSlots.size, content.size)

            if (ifScroll) {
                scrollProgress = value
                recalculateCurrentContent()
                updateOpenGuis()
                true
            } else false

        } else false
    }

    internal abstract fun handleScrollEndReached(newProgress: Int, internalSlotsSize: Int, contentSize: Int): Boolean

    internal fun getItemStack(slot: Int): ItemStack {
        return contentAtSlot(slot)?.let { return@let iconGenerator.invoke(it) }
            ?: ItemStack(Items.AIR)
    }

    internal fun onClickElement(clickEvent: GuiClickEvent) {
        val element = contentAtSlot(clickEvent.slotIndex) ?: kotlin.run {
            return
        }
        onClick?.invoke(clickEvent, element)
    }

    internal fun addSlots(slots: InventorySlotCompound) {
        slots.realSlotsWithInvType(guiType).forEach {
            if (!internalSlots.contains(it))
                internalSlots.add(it)
        }
        internalSlots.sort()
    }

    internal fun registerGui(gui: GuiInstance) {
        registeredGuis += gui
    }

    internal fun unregisterGui(gui: GuiInstance) {
        registeredGuis -= gui
    }

    /**
     * Defines the sort behaviour which gets applied to the content
     * automatically.
     */
    fun <R : Comparable<R>> sortContentBy(reverse: Boolean = false, selector: (E) -> R?) {
        contentSort = {
            if (!reverse) content.sortBy(selector) else content.sortByDescending(selector)
        }
        contentSort.invoke()
    }

    /**
     * Adds a new element to the compound.
     */
    fun addContent(element: E) = addContent(listOf(element))

    /**
     * Adds new elements to the compound.
     */
    fun addContent(elements: Iterable<E>) {
        content += elements
        refreshAfterContentChange()
    }

    /**
     * Removes this element from the compound.
     */
    fun removeContent(element: E) = removeContent(listOf(element))

    /**
     * Removes these elements from the compound.
     */
    fun removeContent(elements: Iterable<E>) {
        content -= elements
        refreshAfterContentChange()
    }

    /**
     * Set the content of the compound to this single element.
     */
    fun setContent(element: E) = setContent(listOf(element))

    /**
     * Set the content of the compound to these elements.
     */
    fun setContent(elements: Iterable<E>) {
        content.clear()
        content += elements
        refreshAfterContentChange()
    }

    private fun refreshAfterContentChange() {
        contentSort.invoke()
        recalculateCurrentContent()
        updateOpenGuis()
    }

}

/**
 * A simple compound element, covering the most common
 * compound use cases.
 *
 * @see GuiPageBuilder.createSimpleCompound
 * @see GuiPageBuilder.createSimpleRectCompound
 */
open class GuiCompoundElement(
    internal val icon: ItemStack,
    internal val onClick: ((GuiClickEvent) -> Unit)? = null
)
