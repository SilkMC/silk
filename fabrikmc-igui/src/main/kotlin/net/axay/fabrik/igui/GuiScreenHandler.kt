package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.events.GuiCloseEvent
import net.axay.fabrik.igui.mixin.ServerPlayerEntityAccessor
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

class GuiScreenHandler(
    val gui: Gui,
    syncId: Int,
    private val playerInv: PlayerInventory,
    inv: Inventory,
) : GenericContainerScreenHandler(
    gui.guiType.screenHandlerType,
    syncId,
    playerInv,
    inv,
    gui.guiType.dimensions.height
) {
    override fun insertItem(
        stack: ItemStack,
        startIndex: Int,
        endIndex: Int,
        fromLast: Boolean,
    ): Boolean {
        if (gui.isOffset) return false

        val slotIndex = if (fromLast) endIndex else startIndex

        val shouldCancel = (startIndex..endIndex).any {
            gui.currentPage.content[it]?.shouldCancel(
                GuiClickEvent(
                    gui,
                    playerInv.player,
                    GuiActionType.INSERT,
                    slotIndex,
                    slots.getOrNull(slotIndex),
                    gui.guiType.dimensions.slotMap[slotIndex]
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
        player: PlayerEntity,
    ) {
        if (gui.isOffset) return

        var shouldCancel = true

        val element = gui.currentPage.content[slotIndex]
        if (element != null) {
            val event = GuiClickEvent(
                gui,
                player,
                GuiActionType.fromSlotActionType(actionType, clickData),
                slotIndex,
                slots.getOrNull(slotIndex),
                gui.guiType.dimensions.slotMap[slotIndex]
            )

            shouldCancel = element.shouldCancel(event)

            element.onClick(event)
            gui.eventHandler.onClick?.invoke(event)
        }

        if (!shouldCancel)
            super.onSlotClick(slotIndex, clickData, actionType, player)
        else {
            updateSyncHandler((player as? ServerPlayerEntityAccessor)?.screenHandlerSyncHandler)
        }
    }

    override fun close(player: PlayerEntity) {
        super.close(player)
        gui.eventHandler.onClose?.invoke(GuiCloseEvent(gui, player))
    }

    override fun canInsertIntoSlot(slot: Slot): Boolean {
        if (gui.isOffset) return false

        val slotIndex = slots.indexOf(slot)

        return gui.currentPage.content[slotIndex]?.shouldCancel(
            GuiClickEvent(
                gui,
                playerInv.player,
                GuiActionType.INSERT,
                slotIndex,
                slot,
                gui.guiType.dimensions.slotMap[slotIndex]
            )
        ) == false
    }

    override fun canInsertIntoSlot(stack: ItemStack, slot: Slot) =
        canInsertIntoSlot(slot)
}
