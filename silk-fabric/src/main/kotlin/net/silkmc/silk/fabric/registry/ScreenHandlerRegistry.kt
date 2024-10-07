@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

fun <T : MenuType<V>, V> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.MENU.register(id, this)
}

fun <V : AbstractContainerMenu, U> MenuType<V>.registerClient(factory: MenuScreens.ScreenConstructor<V, U>) where U : Screen, U : MenuAccess<V> {
    MenuScreens.register(this, factory)
}
