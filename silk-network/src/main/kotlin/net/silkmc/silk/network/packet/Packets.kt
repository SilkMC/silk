package net.silkmc.silk.network.packet

import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.serializer
import net.minecraft.resources.ResourceLocation

/**
 * Creates a new [ServerToClientPacketDefinition]. This packet can only be sent
 * from the server to one or multiple clients. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [ResourceLocation] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> s2cPacket(id: ResourceLocation, cbor: Cbor = Cbor) =
    ServerToClientPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())

/**
 * Creates a new [ClientToServerPacketDefinition]. This packet can only be sent
 * from the client to the current server. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [ResourceLocation] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> c2sPacket(id: ResourceLocation, cbor: Cbor = Cbor) =
    ClientToServerPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())

/**
 * Creates a new [ClientToClientPacketDefinition]. This packet can only be sent
 * from the client to another client. The server will act as the middle man, it is
 * responsible for forwarding this packet. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [ResourceLocation] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> c2cPacket(id: ResourceLocation, cbor: Cbor = Cbor) =
    ClientToClientPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())
