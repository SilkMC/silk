package net.axay.fabrik.test.network

import kotlinx.serialization.Serializable
import net.axay.fabrik.core.logging.logInfo
import net.axay.fabrik.core.logging.logger
import net.axay.fabrik.network.packet.c2sPacket
import net.axay.fabrik.network.packet.s2cPacket
import net.axay.fabrik.test.commands.clientTestCommand
import net.axay.fabrik.test.commands.testCommand
import net.axay.fabrik.test.testmodId

object NetworkTest {
    @Serializable
    private data class TestPacket(
        val city: String,
        val country: String,
        val people: List<String>,
        val houseAmount: Int,
    )

    private val log = logger()

    private val s2cTestPacket = s2cPacket<TestPacket>("testpacket".testmodId)
    private val c2sTestPacket = c2sPacket<TestPacket>("testpacket".testmodId)

    fun initServer() {
        testCommand("network") {
            literal("send") runs {
                s2cTestPacket.sendToAll(TestPacket("Berlin", "Germany", listOf("Merkel", "Scholz"), 3200))
            }
        }

        c2sTestPacket.receiveOnServer {
            log.info("Received test packet on the server-side!")
            logInfo("testPacket = $it")
        }
    }

    fun initClient() {
        clientTestCommand("network") {
            literal("send") runs {
                c2sTestPacket.send(TestPacket("Paris", "France", listOf("Peter"), 40000))
            }
        }

        s2cTestPacket.receiveOnClient {
            log.info("Received test packet on the client-side!")
            logInfo("testPacket = $it")
        }
    }
}
