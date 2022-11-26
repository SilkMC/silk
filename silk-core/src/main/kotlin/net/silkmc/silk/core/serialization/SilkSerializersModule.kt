package net.silkmc.silk.core.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.silkmc.silk.core.serialization.serializers.Vec3iSerializer
import net.silkmc.silk.core.serialization.serializers.Vector3dSerializer
import net.silkmc.silk.core.serialization.serializers.Vector3fSerializer

val silkSerializersModule = SerializersModule {
    contextual(Vec3iSerializer)
    contextual(Vector3fSerializer)
    contextual(Vector3dSerializer)
}

val SilkJson = Json {
    serializersModule = silkSerializersModule
}

@OptIn(ExperimentalSerializationApi::class)
val SilkCbor = Cbor {
    serializersModule = silkSerializersModule
}
