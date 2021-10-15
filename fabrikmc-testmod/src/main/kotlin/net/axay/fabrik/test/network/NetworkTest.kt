package net.axay.fabrik.test.network

import kotlinx.serialization.Serializable
import net.axay.fabrik.core.logging.logInfo
import net.axay.fabrik.core.logging.logger
import net.axay.fabrik.network.packet.s2cPacket
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

    private val testPacket = s2cPacket<TestPacket>("testpacket".testmodId)

    fun initServer() {
        testCommand("network") {
            literal("send") {
                runs {
                    testPacket.sendToAll(TestPacket("Berlin", "Germany", listOf("Merkel", "Scholz"), 3200))
                }
            }
        }
    }

    fun initClient() {
        testPacket.receiveOnClient {
            log.info("Received test packet on the client-side!")
            logInfo("testPacket = $it")
        }
    }
}
