package net.silkmc.silk.paper.internal.igui

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.silkmc.silk.igui.Gui
import net.silkmc.silk.igui.GuiScreenHandler
import org.bukkit.craftbukkit.inventory.CraftInventory
import org.bukkit.craftbukkit.inventory.CraftInventoryView

class PaperGuiScreenHandler(
    gui: Gui,
    syncId: Int,
    inventory: Inventory,
    container: Container,
) : GuiScreenHandler(gui, syncId, inventory, container) {
    private val internalView by lazy {
        CraftInventoryView(inventory.player.bukkitEntity, CraftInventory(container), this)
    }

    override fun getBukkitView() = internalView
}