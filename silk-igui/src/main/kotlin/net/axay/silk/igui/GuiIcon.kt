package net.axay.silk.igui

import kotlinx.coroutines.runBlocking
import net.axay.silk.core.text.literal
import net.axay.silk.igui.observable.GuiProperty
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

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
        private var internalItemStack = iconGenerator.invoke(runBlocking { property.get() })

        override val itemStack get() = internalItemStack

        private val elementListener: suspend (E) -> Unit = {
            internalItemStack = iconGenerator.invoke(it)
            registeredGuis.forEach { gui -> gui.reloadCurrentPage() }
        }

        override fun onChangeUseStatus(inUse: Boolean) {
            if (inUse) property.onChange(elementListener) else property.removeOnChangeListener(elementListener)
        }
    }
}

/**
 * Creates a static gui icon.
 */
val ItemStack.guiIcon get() = GuiIcon.StaticIcon(this.apply {
    if (!hasCustomHoverName())
        setHoverName("".literal)
})

/**
 * Creates a static gui icon.
 */
val Item.guiIcon get() = this.defaultInstance.guiIcon

/**
 * Creates a gui icon which automatically updates itself
 * if the value of the property changes.
 */
fun <E> GuiProperty<E>.guiIcon(iconGenerator: (E) -> ItemStack) =
    GuiIcon.VariableIcon(this, iconGenerator)
