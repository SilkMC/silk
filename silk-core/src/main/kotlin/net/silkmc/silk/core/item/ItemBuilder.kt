package net.silkmc.silk.core.item

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.level.ItemLike
import net.silkmc.silk.core.text.LiteralTextBuilder
import net.silkmc.silk.core.text.literalText
import java.util.*

/**
 * A utility function for building complex [item] stacks with the given [amount].
 *
 * ```kotlin
 * itemStack(Items.FEATHER, amount = 64) {
 *     setCustomName("Cool Feather")
 *     enchant(Enchantments.FIRE_ASPECT, 1)
 * }
 * ```
 */
inline fun itemStack(
    item: ItemLike,
    amount: Int = 1,
    builder: ItemStack.() -> Unit,
) = ItemStack(item, amount).apply(builder)

/**
 * Sets the item lore, which is displayed below the display
 * name of the item stack. Each element of the [text] collection represents
 * one line.
 */
fun ItemStack.setLore(text: Collection<Component>) {
    getOrCreateTagElement("display").put(
        "Lore",
        text.mapTo(ListTag()) { StringTag.valueOf(Component.Serializer.toJson(it)) }
    )
}

/**
 * Sets the item lore, which is displayed below the display
 * name of the item stack. Each child of the first children of
 * the generated literal text represents one line.
 */
fun ItemStack.setLore(baseText: String = "", builder: LiteralTextBuilder.() -> Unit) {
    literalText(baseText, builder).siblings.firstOrNull()?.let {
        setLore(it.siblings)
    }
}

/**
 * Opens a [LiteralTextBuilder] to change the custom name of
 * the item stack. See [literalText].
 */
inline fun ItemStack.setCustomName(baseText: String = "", builder: LiteralTextBuilder.() -> Unit = {}): ItemStack =
    setHoverName(literalText(baseText, builder))    

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

/**
 * Configures the `SkullOwner` nbt tag to have the given [texture].
 * The [texture] has to be base64 encoded.
 *
 * You can find a lot of heads with the associated base64 values on
 * [minecraft-heads.com](https://minecraft-heads.com/).
 *
 * Optional, you can specify a [uuid]. This is *not* necessary if this head
 * is just used because of its texture, but you should specify one if the head
 * is associated with an actual player.
 *
 * ```kotlin
 * skullStack.setSkullTexture(
 *     texture = "eyJ0ZXh0dXJlcyI6ey...", // base64 encoded texture json (this example is truncated)
 *     uuid = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5") // this is optional
 * )
 * ```
 */
fun ItemStack.setSkullTexture(
    texture: String,
    uuid: UUID = UUID(0, 0),
) {
    orCreateTag.put("SkullOwner", CompoundTag().apply {
        putUUID("Id", uuid)

        val propsCompound = if (this.contains("Properties"))
            this.getCompound("Properties")
        else
            CompoundTag().also { put("Properties", it) }

        propsCompound.put("textures", ListTag().apply {
            add(CompoundTag().apply {
                putString("Value", texture)
            })
        })
    })
}

/**
 * Configures the `SkullOwner` nbt tag to represent the given player
 * (specified via [uuid]). The [name] can be anything, but it *should* match
 * the actual player name.
 *
 * ```kotlin
 * skullStack.setSkullPlayer(server.playerList.getPlayerByName("Notch"))
 * ```
 */
fun ItemStack.setSkullPlayer(player: ServerPlayer) {
    player.gameProfile.properties.get("textures")
        .map { it.value }
        .forEach(::setSkullTexture)
}
