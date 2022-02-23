package net.axay.fabrik.core.item

import net.axay.fabrik.core.text.LiteralTextBuilder
import net.axay.fabrik.core.text.literalText
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * A utility to function for building more complex
 * item stacks.
 */
inline fun itemStack(
    item: ItemLike,
    amount: Int = 1,
    builder: ItemStack.() -> Unit,
) = ItemStack(item, amount).apply(builder)

/**
 * Sets the item lore, which is displayed below the display
 * name of the item stack.
 */
fun ItemStack.setLore(text: Collection<Component>) {
    getOrCreateTagElement("display").put(
        "Lore",
        text.mapTo(ListTag()) { StringTag.valueOf(Component.Serializer.toJson(it)) }
    )
}

/**
 * Opens a [LiteralTextBuilder] to change the custom name of
 * the item stack.
 */
inline fun ItemStack.setCustomName(baseText: String = "", builder: LiteralTextBuilder.() -> Unit = {}): ItemStack =
    setHoverName(literalText(baseText, builder))
