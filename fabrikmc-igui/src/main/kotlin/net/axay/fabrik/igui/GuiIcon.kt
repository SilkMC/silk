package net.axay.fabrik.igui

import net.axay.fabrik.core.text.literal
import net.axay.fabrik.igui.observable.GuiProperty
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

abstract class GuiIcon : GuiUseable() {
    abstract val itemStack: ItemStack

    class StaticIcon(override val itemStack: ItemStack) : GuiIcon() {
        override fun startUsing(gui: Gui) { }
        override fun stopUsing(gui: Gui) { }
    }

    class VariableIcon<E>(
        val property: GuiProperty<E>,
        val iconGenerator: (E) -> ItemStack
    ) : GuiIcon() {
        private var internalItemStack = iconGenerator.invoke(property.getValue(null, null))

        override val itemStack get() = internalItemStack

        private val elementListener: (E) -> Unit = {
            internalItemStack = iconGenerator.invoke(property.getValue(null, null))
            registeredGuis.forEach { it.reloadCurrentPage() }
        }

        override fun onChangeUseStatus(inUse: Boolean) {
            if (inUse) property.listeners.add(elementListener) else property.listeners.remove(elementListener)
        }
    }
}

/**
 * Creates a static gui icon.
 */
val ItemStack.guiIcon get() = GuiIcon.StaticIcon(this.apply {
    if (!hasCustomName())
        setCustomName("".literal)
})

/**
 * Creates a static gui icon.
 */
val Item.guiIcon get() = this.defaultStack.guiIcon

/**
 * Creates a gui icon which automatically updates itself
 * if the value of the property changes.
 */
fun <E> GuiProperty<E>.guiIcon(iconGenerator: (E) -> ItemStack) =
    GuiIcon.VariableIcon(this, iconGenerator)
