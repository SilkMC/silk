package net.axay.fabrik.igui

data class GuiDimensions(val width: Int, val height: Int) {
    val slotAmount = width * height

    val guiSlots = ArrayList<GuiSlot>().apply {
        for (row in 1..height) for (slotInRow in 1..width) {
            add(GuiSlot(row, slotInRow))
        }
        sortBy { it.slotIndexIn(this@GuiDimensions) }
    }

    val slotMap = HashMap<Int, GuiSlot>().apply {
        guiSlots.forEach {
            put(it.slotIndexIn(this@GuiDimensions)!!, it)
        }
    }
}
