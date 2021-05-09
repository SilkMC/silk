@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.core.text

import net.minecraft.text.*
import net.minecraft.util.Formatting

/**
 * Opens a [LiteralTextBuilder].
 *
 * @param baseText the text you want to begin with, it is okay to let this empty
 * @param builder the builder which can be used to set the style and add child text components
 */
inline fun literalText(
    baseText: String = "",
    builder: LiteralTextBuilder.() -> Unit = { }
) = LiteralTextBuilder(baseText, Style.EMPTY, false).apply(builder).build()

class LiteralTextBuilder(
    private val text: BaseText,
    private val parentStyle: Style,
    private val inheritStyle: Boolean
) {
    constructor(text: String, parentStyle: Style, inheritStyle: Boolean) :
            this(LiteralText(text), parentStyle, inheritStyle)

    var bold: Boolean? = null
    var italic: Boolean? = null
    var underline: Boolean? = null
    var strikethrough: Boolean? = null

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
    var color: Int? = null

    var clickEvent: ClickEvent? = null
    var hoverEvent: HoverEvent? = null

    val currentStyle: Style
        get() = Style.EMPTY
            .withBold(bold)
            .withItalic(italic)
            .let { if (underline == true) it.withFormatting(Formatting.UNDERLINE) else it }
            .let { if (strikethrough == true) it.withFormatting(Formatting.STRIKETHROUGH) else it }
            .let { if (color != null) it.withColor(TextColor.fromRgb(color ?: 0xFFFFFF)) else it }
            .withClickEvent(clickEvent)
            .withHoverEvent(hoverEvent)
            .let { if (inheritStyle) it.withParent(parentStyle) else it }

    val siblingText = LiteralText("")

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
        builder: LiteralTextBuilder.() -> Unit = { }
    ) {
        siblingText.append(LiteralTextBuilder(text, currentStyle, inheritStyle).apply(builder).build())
    }

    /**
     * Append text to the parent.
     *
     * @param text the text instance
     * @param inheritStyle if true, this text will inherit the style from its parent
     * @param builder the builder which can be used to set the style and add child text components
     */
    inline fun text(
        text: Text,
        inheritStyle: Boolean = true,
        builder: LiteralTextBuilder.() -> Unit = { }
    ) {
        if (text is BaseText) {
            siblingText.append(LiteralTextBuilder(text, currentStyle, inheritStyle).apply(builder).build())
        } else {
            siblingText.append(text)
        }
    }

    /**
     * Adds a line break.
     */
    fun newLine() {
        siblingText.append(LiteralText("\n"))
    }

    fun build() = text.apply {
        style = currentStyle
        if (siblingText.siblings.isNotEmpty())
            append(siblingText)
    }
}
