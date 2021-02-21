@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.LiteralText

class GuiInventory(
    val guiInstance: GuiInstance
) : SimpleInventory(guiInstance.gui.data.guiType.dimensions.slotAmount), NamedScreenHandlerFactory {
    val views = HashMap<PlayerEntity, GenericContainerScreenHandler>()

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        val screenHandler = guiInstance.gui.data.guiType.createScreenHandler(guiInstance, syncId, playerInv, this)
        views[player] = screenHandler
        return screenHandler
    }

    override fun getDisplayName() = LiteralText(guiInstance.gui.data.title)

    override fun onClose(player: PlayerEntity) {
        views -= player
    }

    fun closeForViewers() {
        views.entries.forEach {
            it.value.close(it.key)
        }
    }
}
