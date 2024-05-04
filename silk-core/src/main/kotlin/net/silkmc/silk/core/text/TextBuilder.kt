@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.core.text

import net.minecraft.network.chat.*
import net.minecraft.resources.ResourceLocation

/**
 * Opens a [LiteralTextBuilder].
 *
 * @param baseText the text you want to begin with, it is okay to leave this empty or null
 * @param builder the builder which can be used to set the style and add child text components
 */
inline fun literalText(
    baseText: String? = null,
    builder: LiteralTextBuilder.() -> Unit = { }
) = LiteralTextBuilder(baseText, true).apply(builder).build()

class LiteralTextBuilder(
    private val baseText: Component?,
    private val inheritStyle: Boolean,
) {
    constructor(text: String?, inheritStyle: Boolean) :
            this(text?.literal, inheritStyle)

    @Suppress("NOTHING_TO_INLINE") // default should only be initialized when inheritance is disabled
    private inline fun <T> withDefault(default: T): T? = if (inheritStyle) null else default

    var bold: Boolean? = withDefault(false)
    var italic: Boolean? = withDefault(false)
    var underline: Boolean? = withDefault(false)
    var strikethrough: Boolean? = withDefault(false)
    var obfuscated: Boolean? = withDefault(false)

    /**
     * The text color.
     * As this is an [Int] representing an RGB color, this can be set in the following way:
     *
     * e.g. Medium turquoise:
     *  - `color = 0x4BD6CB`
     *  - `color = 4970187`
     *
     * e.g. Crimson:
     *  - `color = 0xF21347`
     *  - `color = 15864647`
     */
    var color: Int? = withDefault(0xFFFFFF)

    /**
     * The resource location of the font for this component in the
     * resource pack or mod resources within `assets/<namespace>/font`.
     * Defaults to `minecraft:default`.
     */
    var font: ResourceLocation? = withDefault(ResourceLocation("minecraft", "default"))
    // var font: ResourceLocation? = withDefault(Style.DEFAULT_FONT)

    /**
     * When the text is shift-clicked by a player, this string is inserted in their chat input.
     * It does not overwrite any existing text the player was writing. This only works in chat messages.
     */
    var insertion: String? = null

    var clickEvent: ClickEvent? = null
    var hoverEvent: HoverEvent? = null

    val currentStyle: Style
        get() = Style.EMPTY
            .withBold(bold)
            .withItalic(italic)
            .withUnderlined(underline)
            .withStrikethrough(strikethrough)
            .withObfuscated(obfuscated)
            .withColor(color?.let { c -> TextColor.fromRgb(c) })
            .withFont(font)
            .withInsertion(insertion)
            .withClickEvent(clickEvent)
            .withHoverEvent(hoverEvent)

    @PublishedApi
    internal val placeholderText: MutableComponent = Component.empty()

    @PublishedApi
    internal val appendTasks = mutableListOf<() -> Unit>()

    @PublishedApi
    internal inline fun append(crossinline appender: () -> Component) {
        appendTasks.add {
            placeholderText.append(appender())
        }
    }

    /**
     * Append text to the parent.
     *
     * @param text the raw text (without formatting)
     * @param inheritStyle if true, this text will inherit the style from its parent
     * @param builder the builder which can be used to set the style and add child text components
     */
    inline fun text(
        text: String = "",
        inheritStyle: Boolean = true,
        crossinline builder: LiteralTextBuilder.() -> Unit = { }
    ) {
        append {
            LiteralTextBuilder(text, inheritStyle)
                .apply(builder).build()
        }
    }

    /**
     * Appends a [MutableComponent] to the parent.
     *
     * @param text the text instance
     * @param inheritStyle if true, this text will inherit the style from its parent
     * @param builder the builder which can be used to set the style and add child text components
     */
    inline fun text(
        text: Component,
        inheritStyle: Boolean = true,
        crossinline builder: LiteralTextBuilder.() -> Unit = { }
    ) {
        append {
            LiteralTextBuilder(text, inheritStyle)
                .apply(builder).build()
        }
    }

    /**
     * Adds a line break.
     */
    fun newLine() {
        append {
            Component.literal("\n")
        }
    }

    /**
     * Adds an empty line.
     */
    fun emptyLine() {
        newLine()
        newLine()
    }

    fun build(): MutableComponent {
        // apply style and append children
        placeholderText.style = currentStyle
        appendTasks.forEach { it() }

        // no children, return (optionally styled) base text
        if (placeholderText.siblings.isEmpty() && baseText is MutableComponent) {
            val returnText = baseText.copy()
            if (placeholderText.style.isEmpty.not()) {
                returnText.style = placeholderText.style.applyTo(returnText.style)
            }
            return returnText
        }

        // children exist, append base text if present and return placeholder
        if (baseText != null) {
            placeholderText.siblings.addFirst(baseText)
        }
        return placeholderText
    }
}
