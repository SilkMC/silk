package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.observable.GuiList
import net.minecraft.item.ItemStack

class GuiCompound<E>(
    val guiType: GuiType,
    val slots: GuiSlotCompound.SlotRange.Rectangle,
    val content: GuiList<E>,
    private val iconGenerator: (E) -> ItemStack,
    private val onClick: ((event: GuiClickEvent, element: E) -> Unit)?
) : GuiUseable() {
    private var slotIndexes = slots.withDimensions(guiType.dimensions)
        .mapNotNull { it.slotIndexIn(guiType.dimensions) }
        .sorted()
    private val slotAmount = slotIndexes.size

    val compoundWidth = (slots.endInclusive.slotInRow - slots.start.slotInRow) + 1
    val compoundHeight = (slots.endInclusive.row - slots.start.row) + 1

    val contentSize get() = content.internalCollection.size

    var scrollProgress = 0
        private set

    var displayedContent = emptyList<E>()
        private set

    private val contentListener: (List<E>) -> Unit = {
        recalculateContent()
        registeredGuis.forEach { it.reloadCurrentPage() }
    }

    init {
        recalculateContent()
    }

    private fun recalculateContent() {
        var sliceUntil = slotAmount + scrollProgress
        if (sliceUntil > contentSize)
            sliceUntil = contentSize

        displayedContent = content.internalCollection.slice(scrollProgress until sliceUntil)
    }

    internal fun scroll(distance: Int): Boolean {
        val newProgress = scrollProgress + distance
        return if (newProgress >= 0) {
            val shouldScroll = if (slotAmount + newProgress <= contentSize)
                true
            else
                (slotAmount + newProgress <= contentSize + (compoundWidth - (contentSize % compoundWidth)))

            if (shouldScroll) {
                scrollProgress = newProgress
                recalculateContent()
                registeredGuis.forEach { it.reloadCurrentPage() }
                true
            } else false
        } else false
    }

    internal fun getItemStack(slotIndex: Int): ItemStack = displayedContent.getOrNull(slotIndexes.indexOf(slotIndex))
        ?.let { iconGenerator.invoke(it) } ?: ItemStack.EMPTY

    internal fun onClickElement(event: GuiClickEvent) {
        val element = displayedContent.getOrNull(slotIndexes.indexOf(event.slotIndex)) ?: return
        onClick?.invoke(event, element)
    }

    override fun onChangeUseStatus(inUse: Boolean) {
        if (inUse) content.listeners.add(contentListener) else content.listeners.remove(contentListener)
    }
}
