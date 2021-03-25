package net.axay.fabrik.igui

import net.axay.fabrik.core.kotlin.max
import net.axay.fabrik.core.kotlin.min

interface GuiSlotCompound {
    fun withDimensions(dimensions: GuiDimensions): Collection<GuiSlot>

    class SlotRange(
        startSlot: GuiSlot,
        endSlot: GuiSlot,
        val type: Type,
    ) : GuiSlotCompound, ClosedRange<GuiSlot> {
        enum class Type {
            LINEAR, RECTANGLE
        }

        override val start: GuiSlot
        override val endInclusive: GuiSlot

        init {
            val minMaxPair = startSlot to endSlot
            start = minMaxPair.min
            endInclusive = minMaxPair.max
        }

        override fun withDimensions(dimensions: GuiDimensions) = ArrayList<GuiSlot>().apply {
            when (type) {
                Type.RECTANGLE -> {
                    // all possible combinations between the two slots form the rectangle
                    for (row in start.row..endInclusive.row)
                        for (slotInRow in start.slotInRow..endInclusive.slotInRow)
                            this += GuiSlot(row, slotInRow)
                }
                Type.LINEAR -> {
                    if (endInclusive.row > start.row) {
                        // from start --->| to end of row
                        for (slotInRow in start.slotInRow..dimensions.width)
                            this += GuiSlot(start.row, slotInRow)
                        // all rows in between
                        if (endInclusive.row > start.row + 1)
                            for (row in start.row + 1 until endInclusive.row)
                                for (slotInRow in 1..dimensions.width)
                                    this += GuiSlot(row, slotInRow)
                        // from start of row |---> to endInclusive
                        for (slotInRow in 1..endInclusive.slotInRow)
                            this += GuiSlot(endInclusive.row, slotInRow)
                    } else if (endInclusive.row == start.row) {
                        // from start ---> to endInclusive in the same row
                        for (slotInRow in start.slotInRow..endInclusive.slotInRow)
                            this += GuiSlot(start.row, slotInRow)
                    }
                }
            }
        }
    }

    class RowSlots(
        val row: Int
    ) : GuiSlotCompound {
        override fun withDimensions(dimensions: GuiDimensions) = ArrayList<GuiSlot>().apply {
            for (slotInRow in 1..dimensions.width)
                this += GuiSlot(row, slotInRow)
        }
    }

    class ColumnSlots(
        val column: Int
    ) : GuiSlotCompound {
        override fun withDimensions(dimensions: GuiDimensions) = ArrayList<GuiSlot>().apply {
            for (row in 1..dimensions.height)
                this += GuiSlot(row, column)
        }
    }

    class BorderSlots(
        val padding: Int
    ) : GuiSlotCompound {
        override fun withDimensions(dimensions: GuiDimensions) = ArrayList<GuiSlot>().apply {
            for (currentPadding in 0 until padding) {
                for (slotInRow in 1 + currentPadding..dimensions.width - currentPadding) {
                    this += GuiSlot(1, slotInRow)
                    this += GuiSlot(dimensions.height, slotInRow)
                }
                for (row in 2 + currentPadding until dimensions.height - currentPadding) {
                    this += GuiSlot(row, 1)
                    this += GuiSlot(row, dimensions.width)
                }
            }
        }
    }

    class CornerSlots(
        val ifBottomLeft: Boolean = false,
        val ifBottomRight: Boolean = false,
        val ifTopLeft: Boolean = false,
        val ifTopRight: Boolean = false,
    ) : GuiSlotCompound {
        override fun withDimensions(dimensions: GuiDimensions) = ArrayList<GuiSlot>().apply {
            if (ifBottomLeft) this += GuiSlot(1, 1)
            if (ifBottomRight) this += GuiSlot(1, dimensions.width)
            if (ifTopLeft) this += GuiSlot(dimensions.height, 1)
            if (ifTopRight) this += GuiSlot(dimensions.height, dimensions.width)
        }
    }

    class AllSlots : GuiSlotCompound {
        override fun withDimensions(dimensions: GuiDimensions) = dimensions.guiSlots
    }
}

/**
 * Creates a new slot range.
 *
 * This range contains all slots having an index between
 * the indexes of the two given slots.
 */
infix fun GuiSlot.lineTo(slot: GuiSlot) =
    GuiSlotCompound.SlotRange(this, slot, GuiSlotCompound.SlotRange.Type.LINEAR)

/**
 * Creates a new slot range.
 *
 * This range contains all slots inside of a rectangle
 * with the two given slots as two opposite corners in the rectangle.
 */
infix fun GuiSlot.rectTo(slot: GuiSlot) =
    GuiSlotCompound.SlotRange(this, slot, GuiSlotCompound.SlotRange.Type.RECTANGLE)

object Slots {
    // ROW
    val RowOne = GuiSlotCompound.RowSlots(1)
    val RowTwo = GuiSlotCompound.RowSlots(2)
    val RowThree = GuiSlotCompound.RowSlots(3)
    val RowFour = GuiSlotCompound.RowSlots(4)
    val RowFive = GuiSlotCompound.RowSlots(5)
    val RowSix = GuiSlotCompound.RowSlots(6)

    // COLUMN
    val ColumnOne = GuiSlotCompound.ColumnSlots(1)
    val ColumnTwo = GuiSlotCompound.ColumnSlots(2)
    val ColumnThree = GuiSlotCompound.ColumnSlots(3)
    val ColumnFour = GuiSlotCompound.ColumnSlots(4)
    val ColumnFive = GuiSlotCompound.ColumnSlots(5)
    val ColumnSix = GuiSlotCompound.ColumnSlots(6)
    val ColumnSeven = GuiSlotCompound.ColumnSlots(7)
    val ColumnEight = GuiSlotCompound.ColumnSlots(8)
    val ColumnNine = GuiSlotCompound.ColumnSlots(9)

    // BORDER
    val BorderPaddingOne = GuiSlotCompound.BorderSlots(1)
    val BorderPaddingTwo = GuiSlotCompound.BorderSlots(2)
    val BorderPaddingThree = GuiSlotCompound.BorderSlots(3)
    val Border = BorderPaddingOne

    // CORNER
    val Corners = GuiSlotCompound.CornerSlots(
        ifBottomLeft = true, ifBottomRight = true, ifTopLeft = true, ifTopRight = true
    )
    val CornersLeft = GuiSlotCompound.CornerSlots(ifBottomLeft = true, ifTopLeft = true)
    val CornersRight = GuiSlotCompound.CornerSlots(ifBottomRight = true, ifTopRight = true)
    val CornersBottom = GuiSlotCompound.CornerSlots(ifBottomLeft = true, ifBottomRight = true)
    val CornersTop = GuiSlotCompound.CornerSlots(ifTopLeft = true, ifTopRight = true)
    val CornerBottomLeft = GuiSlotCompound.CornerSlots(ifBottomLeft = true)
    val CornerBottomRight = GuiSlotCompound.CornerSlots(ifBottomRight = true)
    val CornerTopLeft = GuiSlotCompound.CornerSlots(ifTopLeft = true)
    val CornerTopRight = GuiSlotCompound.CornerSlots(ifTopRight = true)

    // ALL
    val All = GuiSlotCompound.AllSlots()
}
