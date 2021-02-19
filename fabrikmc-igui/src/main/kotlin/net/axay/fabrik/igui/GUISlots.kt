@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.igui

import net.axay.fabrik.core.kotlin.max
import net.axay.fabrik.core.kotlin.min

// INVENTORY

data class InventoryDimensions(val width: Int, val height: Int) {
    val slotAmount = width * height

    val invSlots by lazy {
        ArrayList<InventorySlot>().apply {
            (1..height).forEach { row ->
                (1..width).forEach { slotInRow ->
                    this += InventorySlot(row, slotInRow)
                }
            }
        }
    }

    val invSlotsWithRealSlots by lazy {
        HashMap<InventorySlot, Int>().apply {
            invSlots.forEach { curSlot ->
                curSlot.realSlotIn(this@InventoryDimensions)?.let { this[curSlot] = it }
            }
        }
    }

    val realSlots by lazy { invSlotsWithRealSlots.values }
}

// SLOTS

data class InventorySlot(val row: Int, val slotInRow: Int) : Comparable<InventorySlot> {
    companion object {
        fun fromRealSlot(realSlot: Int, dimensions: InventoryDimensions) =
            dimensions.invSlotsWithRealSlots.toList().find { it.second == realSlot }?.first
    }

    override fun compareTo(other: InventorySlot) = when {
        row > other.row -> 1
        row < other.row -> -1
        else -> when {
            slotInRow > other.slotInRow -> 1
            slotInRow < other.slotInRow -> -1
            else -> 0
        }
    }

    fun realSlotIn(inventoryDimensions: InventoryDimensions): Int? {
        if (!isInDimension(inventoryDimensions)) return null
        val realRow = inventoryDimensions.height - (row - 1)
        val rowsUnder = if (realRow - 1 >= 0) realRow - 1 else 0
        return ((rowsUnder * inventoryDimensions.width) + slotInRow) - 1
    }

    fun isInDimension(inventoryDimensions: InventoryDimensions) =
        (1..inventoryDimensions.width).contains(slotInRow) && (1..inventoryDimensions.height).contains(row)

    fun add(offsetHorizontally: Int, offsetVertically: Int) = InventorySlot(
        row + offsetVertically,
        slotInRow + offsetHorizontally
    )
}

interface InventorySlotCompound {
    fun withInvType(invType: GUIType): Collection<InventorySlot>

    fun realSlotsWithInvType(invType: GUIType) =
        withInvType(invType).mapNotNull { it.realSlotIn(invType.dimensions) }
}

open class SingleInventorySlot internal constructor(
    val inventorySlot: InventorySlot
) : InventorySlotCompound {
    constructor(row: Int, slotInRow: Int) : this(InventorySlot(row, slotInRow))

    private val slotAsList = listOf(inventorySlot)

    override fun withInvType(invType: GUIType) = slotAsList
}

internal enum class InventorySlotRangeType {
    LINEAR,
    RECTANGLE,
}

class InventorySlotRange internal constructor(
    startSlot: SingleInventorySlot,
    endSlot: SingleInventorySlot,
    private val type: InventorySlotRangeType
) : InventorySlotCompound, ClosedRange<InventorySlot> {
    override val start: InventorySlot
    override val endInclusive: InventorySlot

    init {
        val minMaxPair = Pair(startSlot.inventorySlot, endSlot.inventorySlot)
        start = minMaxPair.min
        endInclusive = minMaxPair.max
    }

    override fun withInvType(invType: GUIType) = LinkedHashSet<InventorySlot>().apply {
        when (type) {
            InventorySlotRangeType.RECTANGLE -> {
                // all possible combinations between the two slots
                // -> form a rectangle
                for (row in start.row..endInclusive.row)
                    for (slotInRow in start.slotInRow..endInclusive.slotInRow)
                        this += InventorySlot(row, slotInRow)
            }

            InventorySlotRangeType.LINEAR -> {
                if (endInclusive.row > start.row) {
                    // from start --->| to end of row
                    for (slotInRow in start.slotInRow..invType.dimensions.width)
                        this += InventorySlot(start.row, slotInRow)
                    // all rows in between
                    if (endInclusive.row > start.row + 1)
                        for (row in start.row + 1 until endInclusive.row)
                            for (slotInRow in 1..invType.dimensions.width)
                                this += InventorySlot(row, slotInRow)
                    // from start of row |----> to endInclusive
                    for (slotInRow in 1..endInclusive.slotInRow)
                        this += InventorySlot(endInclusive.row, slotInRow)
                } else if (endInclusive.row == start.row) {
                    // from start ---> to endInclusive in the same row
                    for (slotInRow in start.slotInRow..endInclusive.slotInRow)
                        this += InventorySlot(start.row, slotInRow)
                }
            }
        }
    }
}

/**
 * This range contains all slots having an index between
 * the indeces of the two given slots.
 */
infix fun SingleInventorySlot.linTo(slot: SingleInventorySlot) =
    InventorySlotRange(this, slot, InventorySlotRangeType.LINEAR)

/**
 * This range contains all slots inside of a thought rectangle
 * with the two given slots as two opposite corners of the rectangle.
 */
infix fun SingleInventorySlot.rectTo(slot: SingleInventorySlot) =
    InventorySlotRange(this, slot, InventorySlotRangeType.RECTANGLE)

class InventoryRowSlots internal constructor(
    val row: Int
) : InventorySlotCompound {
    override fun withInvType(invType: GUIType) = HashSet<InventorySlot>().apply {
        for (slotInRow in 1..invType.dimensions.width)
            this += InventorySlot(row, slotInRow)
    }
}

class InventoryColumnSlots internal constructor(
    val column: Int
) : InventorySlotCompound {
    override fun withInvType(invType: GUIType) = HashSet<InventorySlot>().apply {
        for (row in 1..invType.dimensions.height)
            this += InventorySlot(row, column)
    }
}

class InventoryBorderSlots internal constructor(
    val padding: Int
) : InventorySlotCompound {
    override fun withInvType(invType: GUIType) = HashSet<InventorySlot>().apply {
        val dimensions = invType.dimensions

        for (currentPadding in 0 until padding) {
            for (slotInRow in 1 + currentPadding..dimensions.width - currentPadding) {
                this += InventorySlot(1, slotInRow)
                this += InventorySlot(dimensions.height, slotInRow)
            }
            for (row in 2 + currentPadding until dimensions.height - currentPadding) {
                this += InventorySlot(row, 1)
                this += InventorySlot(row, dimensions.width)
            }
        }
    }
}

class InventoryCornerSlots internal constructor(
    val ifBottomLeft: Boolean = false,
    val ifBottomRight: Boolean = false,
    val ifTopLeft: Boolean = false,
    val ifTopRight: Boolean = false
) : InventorySlotCompound {
    override fun withInvType(invType: GUIType) = HashSet<InventorySlot>().apply {
        val dimensions = invType.dimensions

        if (ifBottomLeft) this += InventorySlot(1, 1)
        if (ifBottomRight) this += InventorySlot(1, dimensions.width)
        if (ifTopLeft) this += InventorySlot(dimensions.height, 1)
        if (ifTopRight) this += InventorySlot(dimensions.height, dimensions.width)
    }
}

class InventoryAllSlots : InventorySlotCompound {
    override fun withInvType(invType: GUIType) = invType.dimensions.invSlots
}

object Slots {
    // ROW
    val RowOne = InventoryRowSlots(1)
    val RowTwo = InventoryRowSlots(2)
    val RowThree = InventoryRowSlots(3)
    val RowFour = InventoryRowSlots(4)
    val RowFive = InventoryRowSlots(5)
    val RowSix = InventoryRowSlots(6)

    // COLUMN
    val ColumnOne = InventoryColumnSlots(1)
    val ColumnTwo = InventoryColumnSlots(2)
    val ColumnThree = InventoryColumnSlots(3)
    val ColumnFour = InventoryColumnSlots(4)
    val ColumnFive = InventoryColumnSlots(5)
    val ColumnSix = InventoryColumnSlots(6)
    val ColumnSeven = InventoryColumnSlots(7)
    val ColumnEight = InventoryColumnSlots(8)
    val ColumnNine = InventoryColumnSlots(9)

    // BORDER
    val BorderPaddingOne = InventoryBorderSlots(1)
    val BorderPaddingTwo = InventoryBorderSlots(2)
    val BorderPaddingThree = InventoryBorderSlots(3)
    val Border = BorderPaddingOne

    // CORNER
    val Corners = InventoryCornerSlots()
    val CornersLeft = InventoryCornerSlots(ifBottomLeft = true, ifTopLeft = true)
    val CornersRight = InventoryCornerSlots(ifBottomRight = true, ifTopRight = true)
    val CornersBottom = InventoryCornerSlots(ifBottomLeft = true, ifBottomRight = true)
    val CornersTop = InventoryCornerSlots(ifTopLeft = true, ifTopRight = true)
    val CornerBottomLeft = InventoryCornerSlots(ifBottomLeft = true)
    val CornerBottomRight = InventoryCornerSlots(ifBottomRight = true)
    val CornerTopLeft = InventoryCornerSlots(ifTopLeft = true)
    val CornerTopRight = InventoryCornerSlots(ifTopRight = true)

    // ALL
    val All = InventoryAllSlots()
}
