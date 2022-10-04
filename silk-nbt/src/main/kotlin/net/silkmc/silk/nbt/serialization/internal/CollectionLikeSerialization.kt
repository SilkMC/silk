package net.silkmc.silk.nbt.serialization.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import net.silkmc.silk.nbt.serialization.serializer.CollectionTagSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

internal val byteArraySerializer = serializer<ByteArray>()
internal val intArraySerializer = serializer<IntArray>()
internal val longArraySerializer = serializer<LongArray>()
internal val byteSerializer = serializer<Byte>()
internal val intSerializer = serializer<Int>()
internal val longSerializer = serializer<Long>()

private val collectionLikeSerializerClass: KClass<*> = run {
    for (name in arrayOf(
        "kotlinx.serialization.internal.CollectionLikeSerializer",
        "kotlinx.serialization.internal.ListLikeSerializer"
    )) {
        try {
            return@run Class.forName(name).kotlin
        } catch (_: ClassNotFoundException) {
        }
    }
    error("silk-nbt is incompatible with this version of kotlinx.serialization. Please report this issue together with the fabric-language-kotlin version used.")
}

@Suppress("unchecked_cast")
private val collectionLikeElementSerializerField = collectionLikeSerializerClass.declaredMemberProperties
    .first { it.name == "elementSerializer" }
    .apply { isAccessible = true } as KProperty1<Any, KSerializer<*>>

@OptIn(ExperimentalSerializationApi::class)
internal val Any.elementSerializer: KSerializer<*>?
    get() = if (collectionLikeSerializerClass.isInstance(this)) {
        collectionLikeElementSerializerField.get(this)
    } else if (this is CollectionTagSerializer<*, *>) {
        tag
    } else {
        null
    }

