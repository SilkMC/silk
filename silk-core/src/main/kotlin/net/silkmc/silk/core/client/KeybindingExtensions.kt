@file:Suppress("unused")

package net.silkmc.silk.core.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping

fun keybindingOf(translationKey: String, type: InputConstants.Type, code: Int, category: String): KeyMapping {
    return KeyBindingHelper.registerKeyBinding(KeyMapping(translationKey, type, code, category))
}

fun keybindingOf(translationKey: String, code: Int, category: String): KeyMapping {
    return KeyBindingHelper.registerKeyBinding(KeyMapping(translationKey, code, category))
}

fun keyOf(binding: KeyMapping): InputConstants.Key = KeyBindingHelper.getBoundKeyOf(binding)
