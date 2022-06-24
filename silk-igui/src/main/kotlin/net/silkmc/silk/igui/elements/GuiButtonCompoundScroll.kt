package net.silkmc.silk.igui.elements

import kotlinx.coroutines.cancel
import net.silkmc.silk.core.task.mcCoroutineTask
import net.silkmc.silk.igui.GuiCompound
import net.silkmc.silk.igui.GuiIcon
import kotlin.time.Duration

class GuiButtonCompoundScroll(
    icon: GuiIcon,
    val compound: GuiCompound<*>,
    val reverse: Boolean,
    val speed: Duration,
    val scrollDistance: Int,
    val scrollTimes: Int,
) : GuiButton(
    icon,
    {
        suspend fun scroll() = if (reverse) compound.scroll(-scrollDistance) else compound.scroll(scrollDistance)

        if (scrollTimes > 1) {
            mcCoroutineTask(
                period = speed,
                howOften = scrollTimes.toLong(),
            ) {
                if (!scroll()) cancel()
            }
        } else scroll()
    }
)
