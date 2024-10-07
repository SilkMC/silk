@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

fun creativeModeTabOf(
    displayName: Component? = null,
    icon: ItemStack? = null,
    builder: CreativeModeTab.Builder.() -> Unit,
): CreativeModeTab {
    val itemGroupBuilder = FabricItemGroup.builder()
    if (displayName != null)
        itemGroupBuilder.title(displayName)

    if (icon != null)
        itemGroupBuilder.icon { icon }

    return itemGroupBuilder.apply(builder).build()
}

fun <T : CreativeModeTab> T.register(id: ResourceLocation): T {
    return BuiltInRegistries.CREATIVE_MODE_TAB.register(id, this)
}

fun <T : CreativeModeTab> T.register(id: String): T {
    return BuiltInRegistries.CREATIVE_MODE_TAB.register(id, this)
}
