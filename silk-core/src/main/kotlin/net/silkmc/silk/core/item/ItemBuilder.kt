package net.silkmc.silk.core.item

import com.mojang.authlib.properties.Property
import com.mojang.authlib.properties.PropertyMap
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.level.ItemLike
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.text.LiteralTextBuilder
import net.silkmc.silk.core.text.literalText
import java.util.*
import kotlin.jvm.optionals.getOrNull

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
    set(DataComponents.LORE, ItemLore(text.toList()))
}

/**
 * Opens a [LiteralTextBuilder] to change the custom name of
 * the item stack. See [literalText].
 */
inline fun ItemStack.setCustomName(baseText: String = "", builder: LiteralTextBuilder.() -> Unit = {}): ItemStack {
    set(DataComponents.CUSTOM_NAME, literalText(baseText, builder))
    return this
}

/**
 * Opens a [LiteralTextBuilder] to change the "minecraft:item_name" item component of
 * the item stack. Name set by "minecraft:item_name" would behave as the default name of this item
 * (for example it will not show up when hovering an item frame with this item stack in it).
 * See [literalText].
 */
inline fun ItemStack.setItemName(baseText: String = "", builder: LiteralTextBuilder.() -> Unit = {}): ItemStack {
    set(DataComponents.ITEM_NAME, literalText(baseText, builder))
    return this
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
fun ItemStack.setPotion(potionHolder: Holder<Potion>) {
    val potionContent = getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
    set(DataComponents.POTION_CONTENTS, potionContent.withPotion(potionHolder))
}

@Deprecated(message = "Use the setPotion function with a holder instead. This function searches for the holder in the potion registry for compatibility only!")
fun ItemStack.setPotion(potion: Potion) {
    val potionRegistry = Silk.server?.registryAccess()?.registry(Registries.POTION)?.getOrNull() ?: return
    val id = potionRegistry.getKey(potion) ?: return
    val holder = potionRegistry.getHolder(id).getOrNull() ?: return
    setPotion(holder)
}

/**
 * Configures the `minecraft:profile` item component to have the given [texture].
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
    uuid: UUID? = null,
) {
    set(DataComponents.PROFILE, ResolvableProfile(Optional.of(""), Optional.ofNullable(uuid), PropertyMap().apply {
        this.put("textures", Property("textures", texture))
    }))
}

/**
 * Configures the `minecraft:profile` item component to represent the given player
 * (specified via [uuid]). The [name] can be anything, but it *should* match
 * the actual player name.
 *
 * ```kotlin
 * skullStack.setSkullPlayer(server.playerList.getPlayerByName("Notch"))
 * ```
 */
fun ItemStack.setSkullPlayer(player: ServerPlayer) {
    set(DataComponents.PROFILE, ResolvableProfile(player.gameProfile))
}
