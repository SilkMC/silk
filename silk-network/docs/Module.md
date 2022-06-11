# Module silk-network

Send any serializable class as a packet - using kotlinx.serialization. Provides utilities for sending and receiving
packets.

${dependencyNotice}

## Define a packet

### Create your packet class

You have to create a serializable class representing your packet first.

```kt
\\@Serializable
data class Person(val name: String, val age: Int)
```

### Packet definition instance

Create a packet definition instance, this class holds information about the packet type and the packet id, and provides
you with functions for sending and receiving packets.

#### Server to client

Use the [s2cPacket][net.axay.silk.network.packet.s2cPacket] function.

```kt
val personPacket = s2cPacket<Person>(Identifier("mymod", "personpacket"))
```

#### Client to server

Use the [c2sPacket][net.axay.silk.network.packet.c2sPacket] function.

```kt
val personPacket = c2sPacket<Person>(Identifier("mymod", "personpacket"))
```

## Send a packet

Once you have packet definition instance, you can send packets.

### Server to client

Send to a specific player:

```kt
personPacket.send(Person("John", 21), player)
```

Send to all players:

```kt
personPacket.sendToAll(Person("Maria", 21))
```

### Client to server

```kt
personPacket.send(Person("Holger", 52))
```

## Receive a packet

Using the packet instance, you can also register a packet receiver.

This can be done using
the [receiveOnClient][net.axay.silk.network.packet.ServerToClientPacketDefinition.receiveOnClient] or
[receiveOnServer][net.axay.silk.network.packet.ClientToServerPacketDefinition.receiveOnServer] function.

### Server to client

```kt
personPacket.receiveOnClient { packet, context ->
    println(packet)
}
```

### Client to server

```kt
personPacket.receiveOnServer { packet, context ->
    println(packet)
}
```

# Package net.axay.silk.network.packet

Utilities for creating client-to-server and server-to-client packets
