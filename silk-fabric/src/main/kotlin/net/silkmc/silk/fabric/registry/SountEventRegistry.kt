@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

fun soundEventOf(
    id: ResourceLocation,
    range: Float? = null,
): SoundEvent {
    return if (range != null)
        SoundEvent.createFixedRangeEvent(id, range)
    else
        SoundEvent.createVariableRangeEvent(id)
}

fun soundEventOf(
    id: String,
    range: Float? = null,
): SoundEvent {
    return soundEventOf(ResourceLocation.parse(id), range)
}

fun SoundEvent.register(id: ResourceLocation = this.location): SoundEvent {
    return BuiltInRegistries.SOUND_EVENT.register(id, this)
}

fun SoundEvent.register(id: String): SoundEvent {
    return BuiltInRegistries.SOUND_EVENT.register(id, this)
}


fun SoundEvent.registerForHolder(id: ResourceLocation = this.location): Holder.Reference<SoundEvent> {
    return BuiltInRegistries.SOUND_EVENT.registerForHolder(id, this)
}

fun SoundEvent.registerForHolder(id: String): Holder.Reference<SoundEvent> {
    return BuiltInRegistries.SOUND_EVENT.registerForHolder(id, this)
}
