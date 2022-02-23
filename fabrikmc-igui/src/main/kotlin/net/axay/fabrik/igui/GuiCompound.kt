package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.observable.AbstractGuiList
import net.minecraft.world.item.ItemStack

class GuiCompound<E>(
    val guiType: GuiType,
    val slots: GuiSlotCompound.SlotRange.Rectangle,
    val content: AbstractGuiList<E, List<E>>,
    private val iconGenerator: suspend (E) -> ItemStack,
    private val onClick: (suspend (event: GuiClickEvent, element: E) -> Unit)?
) : GuiUseable() {
    private var slotIndexes = slots.withDimensions(guiType.dimensions)
        .mapNotNull { it.slotIndexIn(guiType.dimensions) }
        .sorted()
    private val slotAmount = slotIndexes.size

    val compoundWidth = (slots.endInclusive.slotInRow - slots.start.slotInRow) + 1
    val compoundHeight = (slots.endInclusive.row - slots.start.row) + 1

    val contentSize get() = content.collection.size

    var scrollProgress = 0
        private set

    var displayedContent = emptyList<E>()
        private set

    private val contentListener: suspend (List<E>) -> Unit = {
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

        displayedContent = content.collection.slice(scrollProgress until sliceUntil)
    }

    internal suspend fun scroll(distance: Int): Boolean {
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

    internal suspend fun getItemStack(slotIndex: Int): ItemStack = displayedContent.getOrNull(slotIndexes.indexOf(slotIndex))
        ?.let { iconGenerator.invoke(it) } ?: ItemStack.EMPTY

    internal suspend fun onClickElement(event: GuiClickEvent) {
        val element = displayedContent.getOrNull(slotIndexes.indexOf(event.slotIndex)) ?: return
        onClick?.invoke(event, element)
    }

    override fun onChangeUseStatus(inUse: Boolean) {
        if (inUse) content.onChange(contentListener) else content.removeOnChangeListener(contentListener)
    }
}
