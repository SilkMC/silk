package net.axay.fabrik.test.commands

import kotlinx.serialization.Serializable
import net.axay.fabrik.core.text.sendText
import net.axay.fabrik.persistence.compoundKey
import net.axay.fabrik.persistence.persistentCompound
import net.axay.fabrik.test.testmodId

@Serializable
private data class Person(val name: String, val age: Int, val friends: List<String>)

private val simpleIntKey = compoundKey<Int>("simpleint".testmodId)
private val personKey = compoundKey<Person>("person".testmodId)

val persistenceTestCommand = testCommand("persistence") {
    literal("player") {
        literal("set") runs {
            source.player.persistentCompound.let {
                it[simpleIntKey] = 1234

                it[personKey] = Person(
                    "John",
                    32,
                    listOf("Peter", "James", "Sofia")
                )
            }
        }

        literal("get") runs {
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

        literal("remove") runs {
            source.player.persistentCompound.let {
                it -= simpleIntKey
                it -= personKey
            }
        }
    }
}
