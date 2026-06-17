package net.silkmc.silk.igui

import kotlinx.coroutines.launch
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.silkmc.silk.core.task.mcCoroutineScope
import net.silkmc.silk.igui.events.GuiClickEvent
import net.silkmc.silk.igui.events.GuiCloseEvent

class GuiScreenHandler(
    val gui: Gui,
    syncId: Int,
    private val inventory: Inventory,
    val container: Container,
) : AbstractContainerMenu(
    gui.guiType.screenHandlerType,
    syncId
) {
    init {
        container.startOpen(inventory.player)

        for (slotIndex in 0 until container.containerSize) {
            addSlot(Slot(container, slotIndex, 0, 0))
        }

        addStandardInventorySlots(inventory, 0, 0)
    }

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

    override fun quickMoveStack(
        player: Player,
        slotIndex: Int,
    ): ItemStack {
        if (gui.isOffset) return ItemStack.EMPTY

        val slot = slots.getOrNull(slotIndex) ?: return ItemStack.EMPTY
        if (!slot.hasItem()) return ItemStack.EMPTY

        val shouldCancel = gui.currentPage.content[slotIndex]?.shouldCancel(
            GuiClickEvent(
                gui,
                inventory.player,
                GuiActionType.SHIFT_CLICK,
                slotIndex,
                slot,
                gui.guiType.dimensions.slotMap[slotIndex]
            )
        ) == true
        if (shouldCancel) return ItemStack.EMPTY

        val containerSize = container.containerSize
        val stack = slot.item
        val original = stack.copy()

        if (slotIndex < containerSize) {
            if (!moveItemStackTo(stack, containerSize, slots.size, true)) return ItemStack.EMPTY
        } else if (!moveItemStackTo(stack, 0, containerSize, false)) {
            return ItemStack.EMPTY
        }

        if (stack.isEmpty) {
            slot.setByPlayer(ItemStack.EMPTY)
        } else {
            slot.setChanged()
        }

        return original
    }

    override fun clicked(slotIndex: Int, button: Int, actionType: ContainerInput, player: Player) {
        if (gui.isOffset) return

        var shouldCancel = true

        val element = gui.currentPage.content[slotIndex]
        if (element != null) {
            val guiActionType = GuiActionType.fromSlotActionType(actionType, button)

            val event = GuiClickEvent(
                gui,
                player,
                guiActionType,
                slotIndex,
                slots.getOrNull(slotIndex),
                gui.guiType.dimensions.slotMap[slotIndex]
            )

            shouldCancel = element.shouldCancel(event)

            mcCoroutineScope.launch {
                element.onClick(event)
                gui.eventHandler.onClick?.invoke(event)
            }

            if (shouldCancel) {
                when (guiActionType) {
                    GuiActionType.HOTKEY_SWAP -> player.inventoryMenu.sendAllDataToRemote()
                    else -> Unit
                }
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
        container.stopOpen(player)
        mcCoroutineScope.launch {
            gui.eventHandler.onClose?.invoke(GuiCloseEvent(gui, player))
        }
    }

    override fun stillValid(player: Player): Boolean {
        return container.stillValid(player)
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
