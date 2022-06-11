package net.axay.silk.igui.elements

import net.axay.silk.igui.GuiIcon
import net.axay.silk.igui.events.GuiClickEvent

open class GuiButton(
    icon: GuiIcon,
    private val onClick: suspend (GuiClickEvent) -> Unit,
) : GuiPlaceholder(icon) {
    override suspend fun onClick(clickEvent: GuiClickEvent) {
        onClick.invoke(clickEvent)
    }
}
