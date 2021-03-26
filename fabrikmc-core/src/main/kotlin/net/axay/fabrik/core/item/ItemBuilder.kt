package net.axay.fabrik.core.item

import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.text.Text

/**
 * A utility to function for building more complex
 * item stacks.
 */
inline fun itemStack(
    item: ItemConvertible,
    amount: Int = 1,
    builder: ItemStack.() -> Unit,
) = ItemStack(item, amount).apply(builder)

/**
 * Sets the item lore, which is displayed below the display
 * name of the item stack.
 */
fun ItemStack.setLore(text: Collection<Text>) {
    getOrCreateSubTag("display").put(
        "Lore",
        text.mapTo(ListTag()) { StringTag.of(Text.Serializer.toJson(it)) }
    )
}
