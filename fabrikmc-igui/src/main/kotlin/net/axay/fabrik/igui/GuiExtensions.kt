package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity

fun PlayerEntity.openGui(gui: Gui, page: Int? = null) {
    return openGuiInstance(gui.getInstance(this), page)
}

internal fun PlayerEntity.openGuiInstance(guiInstance: GuiInstance, page: Int? = null) {
    if (page != null)
        guiInstance.loadPageUnsafe(page)

    openHandledScreen(guiInstance.inventory)
    return
}
