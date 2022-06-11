package net.axay.silk.igui

data class GuiSlot(
    val row: Int, val slotInRow: Int
) : GuiSlotCompound, Comparable<GuiSlot> {
    override fun withDimensions(dimensions: GuiDimensions) = listOf(this)

    override fun compareTo(other: GuiSlot) = when {
        row > other.row -> 2
        row < other.row -> -2
        else -> when {
            slotInRow > other.slotInRow -> 1
            slotInRow < other.slotInRow -> -1
            else -> 0
        }
    }

    fun isIn(dimensions: GuiDimensions) =
        (1..dimensions.width).contains(slotInRow) && (1..dimensions.height).contains(row)

    fun slotIndexIn(dimensions: GuiDimensions): Int? {
        if (!isIn(dimensions))
            return null
        val realRow = dimensions.height - (row - 1)
        val rowsUnder = if (realRow - 1 >= 0) realRow - 1 else 0
        return ((rowsUnder * dimensions.width) + slotInRow) - 1
    }
}

/**
 * Creates a new gui slot.
 *
 * The first Int is the row - the second Int is the slot in that row.
 */
infix fun Int.sl(slotInRow: Int) = GuiSlot(this, slotInRow)
