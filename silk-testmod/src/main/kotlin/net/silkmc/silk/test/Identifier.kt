package net.silkmc.silk.test

import net.minecraft.resources.ResourceLocation

val String.testmodId
    get() = ResourceLocation.fromNamespaceAndPath("silk-testmod", this)
