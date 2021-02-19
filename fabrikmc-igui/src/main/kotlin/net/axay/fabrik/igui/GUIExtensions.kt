package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity

fun PlayerEntity.openGUI(gui: GUI, page: Int? = null) {
    return openGUIInstance(gui.getInstance(this), page)
}

internal fun PlayerEntity.openGUIInstance(guiInstance: GUIInstance, page: Int? = null) {
    if (page != null)
        guiInstance.loadPageUnsafe(page)

    openHandledScreen(guiInstance.inventory)
    return
}
