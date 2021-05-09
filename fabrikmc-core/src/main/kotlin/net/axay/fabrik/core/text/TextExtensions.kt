package net.axay.fabrik.core.text

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import org.apache.commons.lang3.text.WordUtils

/**
 * Sends the given [LiteralText] to the player.
 */
fun PlayerEntity.sendText(text: Text) {
    sendMessage(text, false)
}

/**
 * Opens a [LiteralTextBuilder] and sends the resulting [LiteralText]
 * to the player.
 *
 * @see [literalText]
 */
inline fun PlayerEntity.sendText(baseText: String = "", builder: (LiteralTextBuilder) -> Unit) {
    sendMessage(literalText(baseText, builder), false)
}

/**
 * Converts this string to a [LiteralText] instance.
 */
val String.literal get() = LiteralText(this)

/**
 * Returns a list of [Text] elements which all do not exceed
 * the given width.
 *
 * @param width the maximum width of one line
 * @param cutLongWords if true, the maximum width will always be enforced
 * but cutting long words in the middle
 * @param lineBuilder responsible for building the literal of each line
 */
inline fun String.literalLines(
    width: Int = 40,
    cutLongWords: Boolean = true,
    lineBuilder: (line: String) -> Text = { it.literal }
): List<Text> = WordUtils.wrap(this, width, System.lineSeparator(), cutLongWords).split(System.lineSeparator())
    .map(lineBuilder)
