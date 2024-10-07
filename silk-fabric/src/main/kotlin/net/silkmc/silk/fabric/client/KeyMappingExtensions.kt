@file:Suppress("unused")

package net.silkmc.silk.fabric.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping

fun keyMappingOf(translationKey: String, type: InputConstants.Type = InputConstants.Type.KEYSYM, code: Int, category: String): KeyMapping {
    return KeyBindingHelper.registerKeyBinding(KeyMapping(translationKey, type, code, category))
}

fun KeyMapping.asKey(): InputConstants.Key = KeyBindingHelper.getBoundKeyOf(this)
