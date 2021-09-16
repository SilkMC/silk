package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiIcon
import net.axay.fabrik.igui.events.GuiClickEvent

open class GuiButton(
    icon: GuiIcon,
    private val onClick: suspend (GuiClickEvent) -> Unit,
) : GuiPlaceholder(icon) {
    override suspend fun onClick(clickEvent: GuiClickEvent) {
        onClick.invoke(clickEvent)
    }
}
