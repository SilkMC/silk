package net.axay.silk.igui

import net.axay.silk.core.kotlin.max
import net.axay.silk.core.kotlin.min

interface GuiSlotCompound {
    fun withDimensions(dimensions: GuiDimensions): Collection<GuiSlot>

    abstract class SlotRange(
        startSlot: GuiSlot,
        endSlot: GuiSlot,
    ) : GuiSlotCompound, ClosedRange<GuiSlot> {
        final override val start: GuiSlot
        final override val endInclusive: GuiSlot

        init {
            val minMaxPairRow = startSlot.row to endSlot.row
            val minMaxPairSlotInRow = startSlot.slotInRow to endSlot.slotInRow
            start = GuiSlot(minMaxPairRow.min, minMaxPairSlotInRow.min)
            endInclusive = GuiSlot(minMaxPairRow.max, minMaxPairSlotInRow.max)
        }

        class Rectangle(startSlot: GuiSlot, endSlot: GuiSlot) : SlotRange(startSlot, endSlot) {
            override fun withDimensions(dimensions: GuiDimensions) = ArrayList<GuiSlot>().apply {
                for (row in start.row..endInclusive.row)
                    for (slotInRow in start.slotInRow..endInclusive.slotInRow)
                        this += GuiSlot(row, slotInRow)
            }
        }

        class HollowRectangle(startSlot: GuiSlot, endSlot: GuiSlot) : SlotRange(startSlot, endSlot) {
            override fun withDimensions(dimensions: GuiDimensions) = HashSet<GuiSlot>().apply {
                for (row in start.row..endInclusive.row) {
                    this += GuiSlot(row, start.slotInRow)
                    this += GuiSlot(row, endInclusive.slotInRow)
                }
                for (slotInRow in start.slotInRow..endInclusive.slotInRow) {
                    this += GuiSlot(start.row, slotInRow)
                    this += GuiSlot(endInclusive.row, slotInRow)
                }
            }
        }

        class Line(startSlot: GuiSlot, endSlot: GuiSlot) : SlotRange(startSlot, endSlot) {
            override fun withDimensions(dimensions: GuiDimensions) = ArrayList<GuiSlot>().apply {
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
    GuiSlotCompound.SlotRange.Line(this, slot)

/**
 * Creates a new slot range.
 *
 * This range contains all slots inside a rectangle
 * with the two given slots as two opposite corners in the rectangle.
 */
infix fun GuiSlot.rectTo(slot: GuiSlot) =
    GuiSlotCompound.SlotRange.Rectangle(this, slot)

/**
 * Creates a new slot range.
 *
 * This range contains all slots border slots of a rectangle
 * with the two given slots as two opposite corners in the rectangle.
 * (The result is a hollow rectangle)
 */
infix fun GuiSlot.hrectTo(slot: GuiSlot) =
    GuiSlotCompound.SlotRange.HollowRectangle(this, slot)

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
    val Corners = GuiSlotCompound.CornerSlots(ifBottomLeft = true, ifBottomRight = true, ifTopLeft = true, ifTopRight = true)
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
