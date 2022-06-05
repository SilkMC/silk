package net.axay.fabrik.core.item

import net.axay.fabrik.core.text.LiteralTextBuilder
import net.axay.fabrik.core.text.literalText
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
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

fun ItemStack.setSkullTexture(texture: String) {
    orCreateTag.put("SkullOwner", nbtCompound {
        put("Id", UUID.randomUUID().toString())
        compound("Properties"){
            compound("textures"){
                put("Value", texture)
            }
        }
    })
}
    
/**
 * Sets the given potion for this [ItemStack].
 *
 * If you want to pass a custom potion,
 * make sure to register it in the potion registry first.
 *
 * Example usage:
 * ```kotlin
 * itemStack(Items.POTION) {
 *     setPotion(Potions.HEALING)
 * }
 * ```
 */
fun ItemStack.setPotion(potion: Potion) {
    PotionUtils.setPotion(this, potion)
}
