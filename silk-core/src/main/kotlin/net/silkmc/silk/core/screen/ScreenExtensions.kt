@file:Suppress("unused")

package net.silkmc.silk.core.screen

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.world.inventory.Slot
import net.silkmc.silk.core.mixin.client.AbstractContainerScreenAccessor

val AbstractContainerScreen<*>.focusedSlot: Slot?
    get() = (this as AbstractContainerScreenAccessor).hoveredSlot
