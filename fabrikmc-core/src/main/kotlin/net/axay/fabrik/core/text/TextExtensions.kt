package net.axay.fabrik.core.text

import net.minecraft.text.LiteralText

val String?.literalText get() = LiteralText(this ?: "")
