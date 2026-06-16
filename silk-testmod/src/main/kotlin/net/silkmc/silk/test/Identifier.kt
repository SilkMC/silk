package net.silkmc.silk.test

import net.minecraft.resources.Identifier

val String.testmodId
    get() = Identifier.fromNamespaceAndPath("silk-testmod", this)
