package net.silkmc.silk.test.commands

import kotlinx.serialization.Serializable
import net.silkmc.silk.core.text.sendText
import net.silkmc.silk.persistence.compoundKey
import net.silkmc.silk.persistence.persistentCompound
import net.silkmc.silk.test.testmodId

@Serializable
private data class Person(val name: String, val age: Int, val friends: List<String>)

private val simpleIntKey = compoundKey<Int>("simpleint".testmodId)
private val personKey = compoundKey<Person>("person".testmodId)

val persistenceTestCommand = testCommand("persistence") {
    literal("player") {
        literal("set") runs {
            source.playerOrException.persistentCompound.let {
                it[simpleIntKey] = 1234

                it[personKey] = Person(
                    "John",
                    32,
                    listOf("Peter", "James", "Sofia")
                )
            }
        }

        literal("get") runs {
            source.playerOrException.persistentCompound.let {
                val simpleInt = it[simpleIntKey]
                val complexClass = it[personKey]

                source.playerOrException.sendText {
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
            source.playerOrException.persistentCompound.let {
                it -= simpleIntKey
                it -= personKey
            }
        }
    }
}
