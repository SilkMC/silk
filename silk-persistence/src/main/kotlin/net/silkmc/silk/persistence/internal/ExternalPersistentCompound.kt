package net.silkmc.silk.persistence.internal

import net.minecraft.nbt.CompoundTag
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.persistence.CompoundKey
import net.silkmc.silk.persistence.PersistentCompound

@InternalSilkApi
class ExternalPersistentCompound(
    private val read: () -> CompoundTag?,
    private val write: (CompoundTag) -> Unit,
) : PersistentCompound() {
    override var data: CompoundTag?
        get() = read() ?: CompoundTag()
        set(value) = write(value ?: CompoundTag())

    override fun loadFromCompound(nbtCompound: CompoundTag, loadRaw: Boolean) = Unit
    override fun putInCompound(nbtCompound: CompoundTag, writeRaw: Boolean) = Unit

    override fun onChanged(key: CompoundKey<*>?, value: Any?) {
        val current = data!!
        if (key != null) {
            @Suppress("UNCHECKED_CAST")
            if (value != null) current.put(key.name, (key as CompoundKey<Any>).convertValueToNbtElement(value))
            else current.remove(key.name)
        }

        write(current)
    }
}
