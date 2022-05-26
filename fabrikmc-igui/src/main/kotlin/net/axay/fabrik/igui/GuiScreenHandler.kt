package net.axay.fabrik.igui

import kotlinx.coroutines.launch
import net.axay.fabrik.core.task.mcCoroutineScope
import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.events.GuiCloseEvent
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class GuiScreenHandler(
    val gui: Gui,
    syncId: Int,
    private val inventory: Inventory,
    container: Container,
) : ChestMenu(
    gui.guiType.screenHandlerType,
    syncId,
    inventory,
    container,
    gui.guiType.dimensions.height
) {
    override fun moveItemStackTo(
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
                    inventory.player,
                    GuiActionType.INSERT,
                    slotIndex,
                    slots.getOrNull(slotIndex),
                    gui.guiType.dimensions.slotMap[slotIndex]
                )
            ) == true
        }

        return if (!shouldCancel)
            super.moveItemStackTo(stack, startIndex, endIndex, fromLast)
        else false
    }

    override fun clicked(slotIndex: Int, button: Int, actionType: ClickType, player: Player) {
        if (gui.isOffset) return

        var shouldCancel = true

        val element = gui.currentPage.content[slotIndex]
        if (element != null) {
            val event = GuiClickEvent(
                gui,
                player,
                GuiActionType.fromSlotActionType(actionType, button),
                slotIndex,
                slots.getOrNull(slotIndex),
                gui.guiType.dimensions.slotMap[slotIndex]
            )

            shouldCancel = element.shouldCancel(event)

            mcCoroutineScope.launch {
                element.onClick(event)
                gui.eventHandler.onClick?.invoke(event)
            }
        }

        if (!shouldCancel)
            super.clicked(slotIndex, button, actionType, player)
        else {
            sendAllDataToRemote()
        }
    }

    override fun removed(player: Player) {
        super.removed(player)
        mcCoroutineScope.launch {
            gui.eventHandler.onClose?.invoke(GuiCloseEvent(gui, player))
        }
    }

    private fun isActionValid(slot: Slot, guiActionType: GuiActionType): Boolean {
        if (gui.isOffset) return false

        val slotIndex = slots.indexOf(slot)

        return gui.currentPage.content[slotIndex]?.shouldCancel(
            GuiClickEvent(
                gui,
                inventory.player,
                guiActionType,
                slotIndex,
                slot,
                gui.guiType.dimensions.slotMap[slotIndex]
            )
        ) == false
    }

    override fun canDragTo(slot: Slot) =
        isActionValid(slot, GuiActionType.DRAG)

    override fun canTakeItemForPickAll(stack: ItemStack, slot: Slot) =
        isActionValid(slot, GuiActionType.PICKUP_ALL)
}
