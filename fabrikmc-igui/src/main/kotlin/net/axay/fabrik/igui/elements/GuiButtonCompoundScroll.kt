package net.axay.fabrik.igui.elements

import net.axay.fabrik.core.task.coroutineTask
import net.axay.fabrik.igui.GuiCompound
import net.minecraft.item.ItemStack

class GuiButtonCompoundScroll(
    itemStack: ItemStack,
    val compound: GuiCompound<*>,
    val reverse: Boolean,
    val speed: Long,
    val scrollDistance: Int,
    val scrollTimes: Int,
) : GuiButton(
    itemStack,
    {
        fun scroll() = if (reverse) compound.scroll(-scrollDistance) else compound.scroll(scrollDistance)

        if (scrollTimes > 1) {
            coroutineTask(
                period = speed,
                howOften = scrollTimes.toLong()
            ) {
                if (!scroll())
                    it.isCancelled = true
            }
        } else scroll()
    }
)
