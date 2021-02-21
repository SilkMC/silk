package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity

class GuiScreenHandler(
    val guiInstance: GuiInstance,
    syncId: Int,
    private val playerInv: PlayerInventory,
    inv: Inventory
) : GenericContainerScreenHandler(
    guiInstance.gui.data.guiType.screenHandlerType, syncId,
    playerInv, inv,
    guiInstance.gui.data.guiType.dimensions.height
) {
    val Slot.posIndex get() = slots.indexOf(this)

    override fun insertItem(stack: ItemStack, startIndex: Int, endIndex: Int, fromLast: Boolean): Boolean {
        if (guiInstance.isInMove) return false

        val shouldCancel = (startIndex..endIndex).any {
            guiInstance.currentPage.slots[it]?.shouldCancel(
                GuiClickEvent(
                    guiInstance, playerInv.player, GuiActionType.INSERT,
                    if (fromLast) endIndex else startIndex
                )
            ) == true
        }

        return if (!shouldCancel)
            super.insertItem(stack, startIndex, endIndex, fromLast)
        else false
    }

    override fun onSlotClick(
        slotIndex: Int,
        clickData: Int,
        actionType: SlotActionType,
        player: PlayerEntity
    ): ItemStack {
        if (guiInstance.isInMove) return ItemStack.EMPTY

        val slot = guiInstance.currentPage.slots[slotIndex]
        val event =
            GuiClickEvent(guiInstance, player, GuiActionType.fromSlotActionType(actionType, clickData), slotIndex)

        slot?.onClick(event)

        return if (slot?.shouldCancel(event) != true)
            super.onSlotClick(slotIndex, clickData, actionType, player)
        else {
            (player as? ServerPlayerEntity)?.refreshScreenHandler(this)
            ItemStack.EMPTY
        }
    }

    override fun close(player: PlayerEntity) {
        if (guiInstance.gui is GuiIndividual)
            guiInstance.gui.deleteInstance(player)
    }

    override fun canInsertIntoSlot(slot: Slot): Boolean {
        if (guiInstance.isInMove) return false

        val slotIndex = slot.posIndex

        return guiInstance.currentPage.slots[slotIndex]?.shouldCancel(
            GuiClickEvent(guiInstance, playerInv.player, GuiActionType.INSERT, slotIndex)
        ) == false
    }

    override fun canInsertIntoSlot(stack: ItemStack, slot: Slot) =
        canInsertIntoSlot(slot)
}
