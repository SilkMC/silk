package net.silkmc.silk.core.text

import com.google.gson.GsonBuilder
import com.mojang.serialization.JsonOps
import net.minecraft.commands.CommandSource
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.annotations.DelicateSilkApi
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
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
 *
 * This function currently simply calls the [CommandSource.sendSystemMessage] function.
 * It exists to provide a more consistent API with the more complex [sendText] builder
 * function.
 * Additionally, it will remain stable in case Minecraft changes the API in the future.
 */
fun CommandSource.sendText(text: Component) {
    sendSystemMessage(text)
}

/**
 * Opens a [LiteralTextBuilder] and sends the resulting [MutableComponent]
 * to the player.
 *
 * @see [literalText]
 */
inline fun CommandSource.sendText(baseText: String? = null, builder: LiteralTextBuilder.() -> Unit = { }) {
    sendSystemMessage(literalText(baseText, builder))
}

/**
 * Sends the given [Component] to the player.
 *
 * This function currently simply calls the [CommandSource.sendSystemMessage] function.
 * It exists to provide a more consistent API with the more complex [sendText] builder
 * function.
 * Additionally, it will remain stable in case Minecraft changes the API in the future.
 */
fun ServerPlayer.sendText(text: Component) {
    sendSystemMessage(text)
}

/**
 * Opens a [LiteralTextBuilder] and sends the resulting [MutableComponent]
 * to the player.
 *
 * @see [literalText]
 */
inline fun ServerPlayer.sendText(baseText: String? = null, builder: LiteralTextBuilder.() -> Unit = { }) {
    sendSystemMessage(literalText(baseText, builder))
}

/**
 * Opens a [LiteralTextBuilder] and sends the resulting [Component]
 * to each player on the server.
 *
 * @see [literalText]
 */
inline fun MinecraftServer.broadcastText(baseText: String? = null, builder: LiteralTextBuilder.() -> Unit= { }) {
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

private val prettyPrintGson = GsonBuilder().setPrettyPrinting().create()
private val emptyRegistryOps = RegistryAccess.EMPTY.createSerializationContext(JsonOps.INSTANCE)

/**
 * Returns a pretty printed JSON string of this [Component]
 * in its serialized form.
 */
@DelicateSilkApi
@ExperimentalSilkApi
fun Component.serializeToPrettyJson(): String {
    val json = ComponentSerialization.CODEC.encodeStart(emptyRegistryOps, this).getOrThrow()
    return prettyPrintGson.toJson(json)
}
