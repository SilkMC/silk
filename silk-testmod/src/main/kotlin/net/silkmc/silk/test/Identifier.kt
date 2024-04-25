package net.silkmc.silk.test

import net.minecraft.resources.ResourceLocation

val String.testmodId
    get() = ResourceLocation("silk-testmod", this)
