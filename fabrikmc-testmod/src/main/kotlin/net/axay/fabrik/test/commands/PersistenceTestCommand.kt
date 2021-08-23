package net.axay.fabrik.test.commands

import kotlinx.serialization.Serializable
import net.axay.fabrik.commands.command
import net.axay.fabrik.commands.literal
import net.axay.fabrik.commands.simpleExecutes
import net.axay.fabrik.core.text.sendText
import net.axay.fabrik.persistence.compoundKey
import net.axay.fabrik.persistence.persistentCompound
import net.axay.fabrik.test.testmodId

@Serializable
private data class Person(val name: String, val age: Int, val friends: List<String>)

private val simpleIntKey = compoundKey<Int>("simpleInt".testmodId)
private val personKey = compoundKey<Person>("person".testmodId)

val persistenceTestCommand = command("persistence") {
    literal("player") {
        literal("set") {
            simpleExecutes {
                source.player.persistentCompound.let {
                    it[simpleIntKey] = 1234

                    it[personKey] = Person(
                        "John",
                        32,
                        listOf("Peter", "James", "Sofia")
                    )
                }
            }
        }

        literal("get") {
            simpleExecutes {
                source.player.persistentCompound.let {
                    val simpleInt = it[simpleIntKey]
                    val complexClass = it[personKey]

                    source.player.sendText {
                        text("simple_int = ")
                        text("$simpleInt") {
                            bold = true
                        }
                        newLine()
                        text("complex_class = ")
                        text("$complexClass") {
                            bold = true
                        }
                    }
                }
            }
        }
    }
}
