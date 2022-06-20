package net.silkmc.silk.igui.elements

import net.silkmc.silk.igui.GuiIcon
import net.silkmc.silk.igui.events.GuiClickEvent

open class GuiButton(
    icon: GuiIcon,
    private val onClick: suspend (GuiClickEvent) -> Unit,
) : GuiPlaceholder(icon) {
    override suspend fun onClick(clickEvent: GuiClickEvent) {
        onClick.invoke(clickEvent)
    }
}
