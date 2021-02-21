package net.axay.fabrik.igui.elements

import net.axay.fabrik.core.task.coroutineTask
import net.minecraft.item.ItemStack

class GuiSpaceCompoundScrollButton(

    icon: ItemStack,

    private val compound: AbstractGuiSpaceCompound<*>,
    private val scrollDistance: Int,
    private val scrollTimes: Int,
    private val reverse: Boolean = false

) : GuiButton(icon, {

    if (scrollTimes > 1) {
        coroutineTask(
            period = 50,
            howOften = scrollTimes.toLong()
        ) {
            val ifScrolled = if (reverse) compound.scroll(-scrollDistance) else compound.scroll(scrollDistance)
            if (!ifScrolled) it.isCancelled = true
        }
    } else if (scrollTimes == 1)
        if (reverse) compound.scroll(-scrollDistance) else compound.scroll(scrollDistance)

}) {

    constructor(
        icon: ItemStack,
        compound: GuiRectSpaceCompound<*>,
        scrollTimes: Int = 1,
        reverse: Boolean = false
    ) : this(icon, compound, compound.compoundWidth, scrollTimes, reverse)

}
