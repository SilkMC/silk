@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.fabrik.core.text

import net.minecraft.text.*
import net.minecraft.util.Formatting

inline fun fabrikText(
    baseText: String = "",
    builder: LiteralTextBuilder.() -> Unit = { }
) = LiteralTextBuilder(baseText, Style.EMPTY, false).apply(builder).build()

class LiteralTextBuilder(
    private val text: String,
    private val parentStyle: Style,
    private val inheritStyle: Boolean
) {
    var bold: Boolean? = null
    var italic: Boolean? = null
    var underline: Boolean? = null
    var strikethrough: Boolean? = null

    var color: Int? = null

    var clickEvent: ClickEvent? = null
    var hoverEvent: HoverEvent? = null

    val currentStyle: Style
        get() = Style.EMPTY
            .withBold(bold)
            .withItalic(italic)
            .withUnderline(underline)
            .let { if (strikethrough == true) it.withFormatting(Formatting.STRIKETHROUGH) else it }
            .let { if (color != null) it.withColor(TextColor.fromRgb(color ?: 0xFFFFFF)) else it }
            .withClickEvent(clickEvent)
            .withHoverEvent(hoverEvent)
            .let { if (inheritStyle) it.withParent(parentStyle) else it }

    val siblingText = LiteralText("")

    inline fun literalText(
        text: String = "",
        inheritStyle: Boolean = true,
        builder: LiteralTextBuilder.() -> Unit = { }
    ) {
        siblingText.append(LiteralTextBuilder(text, currentStyle, inheritStyle).apply(builder).build())
    }

    fun build() = LiteralText(text).apply {
        style = currentStyle
        append(siblingText)
    }
}
