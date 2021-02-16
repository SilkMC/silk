package net.axay.fabrik.igui

import net.axay.fabrik.igui.GUI
import net.axay.fabrik.igui.GUIInstance
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryView

fun Player.openGUI(gui: GUI<*>, page: Int? = null): InventoryView? {
    closeInventory()
    return openGUIInstance(gui.getInstance(this), page)
}

internal fun Player.openGUIInstance(guiInstance: GUIInstance<*>, page: Int? = null): InventoryView? {

    if (page != null)
        guiInstance.loadPageUnsafe(page)

    return openInventory(guiInstance.bukkitInventory)

}
