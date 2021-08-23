package net.axay.fabrik.test

import net.minecraft.util.Identifier

val String.testmodId get() = Identifier("fabrikmc-testmod", this)
