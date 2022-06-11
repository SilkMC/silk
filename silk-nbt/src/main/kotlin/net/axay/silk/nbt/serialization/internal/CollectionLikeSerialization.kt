package net.axay.silk.nbt.serialization.internal

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

internal val byteArraySerializer = serializer<ByteArray>()
internal val intArraySerializer = serializer<IntArray>()
internal val longArraySerializer = serializer<LongArray>()
internal val byteSerializer = serializer<Byte>()
internal val intSerializer = serializer<Int>()
internal val longSerializer = serializer<Long>()

private val collectionLikeSerializerClass = Class.forName("kotlinx.serialization.internal.CollectionLikeSerializer").kotlin

@Suppress("unchecked_cast")
private val collectionLikeElementSerializerField = collectionLikeSerializerClass.declaredMemberProperties
    .first { it.name == "elementSerializer" }
    .apply { isAccessible = true } as KProperty1<Any, KSerializer<*>>

internal val Any.elementSerializer: KSerializer<*>?
    get() = if (collectionLikeSerializerClass.isInstance(this)) {
        collectionLikeElementSerializerField.get(this)
    } else {
        null
    }
