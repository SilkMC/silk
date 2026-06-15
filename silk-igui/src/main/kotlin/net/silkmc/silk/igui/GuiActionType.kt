package net.silkmc.silk.igui

import net.minecraft.world.inventory.ContainerInput

enum class GuiActionType {
    /**
     * A normal slot click.
     */
    PICKUP,
    PICKUP_LEFT,
    PICKUP_RIGHT,
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
    SHIFT_CLICK_LEFT,
    SHIFT_CLICK_RIGHT,
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
            slotActionType: ContainerInput,
            button: Int
        ) = when (slotActionType) {
            ContainerInput.PICKUP -> when (button) {
                0 -> PICKUP_LEFT
                1 -> PICKUP_RIGHT
                else -> PICKUP
            }
            ContainerInput.PICKUP_ALL -> PICKUP_ALL
            ContainerInput.QUICK_MOVE -> when (button) {
                0 -> SHIFT_CLICK_LEFT
                1 -> SHIFT_CLICK_RIGHT
                else -> SHIFT_CLICK
            }
            ContainerInput.SWAP -> HOTKEY_SWAP
            ContainerInput.CLONE -> MIDDLE_CLICK
            ContainerInput.THROW -> if (button == 1) THROW_ALL else THROW_ONE
            ContainerInput.QUICK_CRAFT -> when (button) {
                0 -> DRAG_START
                2 -> DRAG_END
                else -> DRAG
            }
        }
    }

    val isLeftClick: Boolean
        get() = this == SHIFT_CLICK_LEFT || this == PICKUP_LEFT

    val isRightClick: Boolean
        get() = this == SHIFT_CLICK_RIGHT || this == PICKUP_RIGHT
}
