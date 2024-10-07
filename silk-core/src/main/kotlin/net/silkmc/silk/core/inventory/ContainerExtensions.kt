@file:Suppress("unused")

package net.silkmc.silk.core.inventory

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.Container
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

operator fun Container.get(slot: Int): ItemStack = getItem(slot)

operator fun Container.set(slot: Int, stack: ItemStack) = setItem(slot, stack)

operator fun Container.iterator(): Iterator<ItemStack> = ContainerIterator(this)

operator fun Container.contains(item: Item): Boolean {
    return hasAnyMatching { other -> !other.isEmpty && ItemStack.isSameItemSameComponents(other, item.defaultInstance) }
}

operator fun Container.contains(stack: ItemStack): Boolean {
    return hasAnyMatching { other -> !other.isEmpty && ItemStack.isSameItemSameComponents(other, stack) }
}

operator fun Container.contains(tag: TagKey<Item>): Boolean {
    return hasAnyMatching { itemStack -> !itemStack.isEmpty && itemStack.`is`(tag) }
}

operator fun Container.contains(item: Holder<Item>): Boolean {
    return hasAnyMatching { itemStack -> !itemStack.isEmpty && itemStack.`is`(item) }
}

operator fun Container.contains(item: HolderSet<Item>): Boolean {
    return hasAnyMatching { itemStack -> !itemStack.isEmpty && itemStack.`is`(item) }
}

operator fun Container.contains(predicate: (ItemStack) -> Boolean): Boolean {
    return hasAnyMatching(predicate)
}

private class ContainerIterator(private val container: Container) : Iterator<ItemStack> {
    var pos = 0

    override fun hasNext() = pos <= container.containerSize

    override fun next(): ItemStack {
        return container[pos++]
    }
}
