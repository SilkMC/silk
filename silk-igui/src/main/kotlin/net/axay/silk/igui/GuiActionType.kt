package net.axay.silk.igui

import net.minecraft.world.inventory.ClickType

enum class GuiActionType {
    /**
     * A normal slot click.
     */
    PICKUP,
    /**
     * A double slot click (to pick up all items of a stack).
     */
    PICKUP_ALL,
    /**
     * Inserting items into an inventory.
     */
    INSERT,
    /**
     * Moving items from one inventory to another
     * using shift.
     */
    SHIFT_CLICK,
    /**
     * Moving items from one inventory to another
     * using a hotkey (e.g. 0 - 9).
     */
    HOTKEY_SWAP,
    /**
     * Cloning an ItemStack by middle clicking it.
     */
    MIDDLE_CLICK,
    /**
     * Throw away a whole ItemStack (e.g. using CTRL + Q).
     */
    THROW_ALL,
    /**
     * Throw away one item of an ItemStack (e.g. using Q).
     */
    THROW_ONE,
    /**
     * Using a dragging feature.
     */
    DRAG,
    /**
     * Start using a dragging feature.
     */
    DRAG_START,
    /**
     * Finish using a dragging feature.
     */
    DRAG_END,

    ;

    companion object {
        fun fromSlotActionType(
            slotActionType: ClickType,
            button: Int
        ) = when (slotActionType) {
            ClickType.PICKUP -> PICKUP
            ClickType.PICKUP_ALL -> PICKUP_ALL
            ClickType.QUICK_MOVE -> SHIFT_CLICK
            ClickType.SWAP -> HOTKEY_SWAP
            ClickType.CLONE -> MIDDLE_CLICK
            ClickType.THROW -> if (button == 1) THROW_ALL else THROW_ONE
            ClickType.QUICK_CRAFT -> when (button) {
                0 -> DRAG_START
                2 -> DRAG_END
                else -> DRAG
            }
        }
    }
}
