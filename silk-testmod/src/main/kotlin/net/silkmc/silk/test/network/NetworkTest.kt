package net.silkmc.silk.test.network

import kotlinx.serialization.Serializable
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.logging.logger
import net.silkmc.silk.network.packet.c2cPacket
import net.silkmc.silk.network.packet.c2sPacket
import net.silkmc.silk.network.packet.s2cPacket
import net.silkmc.silk.test.commands.clientTestCommand
import net.silkmc.silk.test.commands.testCommand
import net.silkmc.silk.test.testmodId

object NetworkTest {
    @Serializable
    private data class TestPacket(
        val city: String,
        val country: String,
        val people: List<String>,
        val houseAmount: Int,
    )

    private val log = logger()

    private val s2cTestPacket = s2cPacket<TestPacket>("testpacket_s2c".testmodId)
    private val c2sTestPacket = c2sPacket<TestPacket>("testpacket_c2s".testmodId)
    private val c2cTestPacket = c2cPacket<TestPacket>("testpacket_c2c".testmodId)

    fun initServer() {
        testCommand("network") {
            literal("send_s2c") runs {
                s2cTestPacket.sendToAll(TestPacket("Berlin", "Germany", listOf("Merkel", "Scholz"), 3200))
            }
        }

        c2sTestPacket.receiveOnServer { packet, _ ->
            log.info("Received c2s test packet on the server-side!")
            log.info("testPacket = $packet")
        }

        c2cTestPacket.forwardOnServer { serializedPacket, context ->
            log.info("Received c2c test packet on the server-side - forwarding...")
            val actualPacket = serializedPacket.deserialize()
            // forward to:
            Silk.players - context.player
        }
    }

    fun initClient() {
        clientTestCommand("network") {
            literal("send_c2s") runs {
                c2sTestPacket.send(TestPacket("Paris", "France", listOf("Peter"), 40000))
            }
            literal("send_c2c") runs {
                c2cTestPacket.send(TestPacket("London", "United Kingdom", listOf("Queen"), 10000))
            }
        }

        s2cTestPacket.receiveOnClient { packet, _ ->
            log.info("Received s2c test packet on the client-side!")
            log.info("testPacket = $packet")
        }

        c2cTestPacket.receiveOnClient { packet, _ ->
            log.info("Received c2c test packet on the client-side!")
            log.info("testPacket = $packet")
        }
    }
}
