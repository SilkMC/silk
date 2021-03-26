package net.axay.fabrik.core.text

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

/**
 * Sends the given [LiteralText] to the player.
 */
fun PlayerEntity.sendText(text: LiteralText) {
    sendMessage(text, false)
}

/**
 * Converts this string to a [LiteralText] instance.
 */
val String.literal get() = LiteralText(this)

/**
 * Returns a list of [Text] elements which all do not exceed
 * the given width.
 */
inline fun String.literalLines(width: Int, lineBuilder: (line: String) -> Text) =
    chunked(width).map { lineBuilder.invoke(it) }
