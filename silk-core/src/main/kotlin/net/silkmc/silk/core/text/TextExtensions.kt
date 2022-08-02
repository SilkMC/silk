package net.silkmc.silk.core.text

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.player.Player
import org.apache.commons.lang3.text.WordUtils

/**
 * Converts this string to a [LiteralText] instance.
 */
val String.literal: MutableComponent get() = Component.literal(this)

/**
 * Returns a list of [Component] elements which all do not exceed
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
    lineBuilder: (line: String) -> Component = { it.literal }
): List<Component> = WordUtils.wrap(this, width, System.lineSeparator(), cutLongWords).split(System.lineSeparator())
    .map(lineBuilder)

/**
 * Sends the given [Component] to the player.
 */
fun Player.sendText(text: Component) {
    displayClientMessage(text, false)
}

/**
 * Opens a [LiteralTextBuilder] and sends the resulting [TextComponent]
 * to the player.
 *
 * @see [literalText]
 */
inline fun Player.sendText(baseText: String = "", builder: LiteralTextBuilder.() -> Unit = { }) {
    displayClientMessage(literalText(baseText, builder), false)
}

/**
 * Opens a [LiteralTextBuilder] and sends the resulting [Component]
 * to each player on the server.
 *
 * @see [literalText]
 */
inline fun MinecraftServer.broadcastText(baseText: String = "", builder: LiteralTextBuilder.() -> Unit= { }) {
    broadcastText(literalText(baseText, builder))
}

/**
 * Sends the given [Component] to each player on the server.
 */
fun MinecraftServer.broadcastText(text: Component) {
    playerList.broadcastSystemMessage(text, false)
}

@Deprecated(
    message = "The function name sendText is inconsistent, use broadcastText instead.",
    replaceWith = ReplaceWith("this.broadcastText(text)")
)
fun MinecraftServer.sendText(text: Component) {
    broadcastText(text)
}
