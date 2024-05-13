package net.silkmc.silk.test.commands

import kotlinx.serialization.Serializable
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.core.entity.blockPos
import net.silkmc.silk.core.text.sendText
import net.silkmc.silk.persistence.PersistentCompound
import net.silkmc.silk.persistence.compoundKey
import net.silkmc.silk.persistence.persistentCompound
import net.silkmc.silk.test.testmodId

@Serializable
private data class Person(val name: String, val age: Int, val friends: List<String>)

private val simpleIntKey = compoundKey<Int>("simpleint".testmodId)
private val personKey = compoundKey<Person>("person".testmodId)

val persistenceTestCommand = testCommand("persistence") {
    literal("player") {
        applyTestSubCommands { it.playerOrException.persistentCompound }
    }
    literal("world") {
        applyTestSubCommands { it.level.persistentCompound }
    }
    literal("chunk") {
        applyTestSubCommands { it.level.getChunk(it.playerOrException.blockPos).persistentCompound }
    }
}

fun LiteralCommandBuilder<CommandSourceStack>.applyTestSubCommands(
    compoundGetter: (CommandSourceStack) -> PersistentCompound) {
    literal("set") runs {
        compoundGetter(source).testSet()
    }
    literal("get") runs {
        compoundGetter(source).testGet(source.playerOrException)
    }
    literal("remove") runs {
        compoundGetter(source).testRemove()
    }
}

private fun PersistentCompound.testSet() {
    this[simpleIntKey] = 1234
    this[personKey] = Person(
        "John",
        32,
        listOf("Peter", "James", "Sofia")
    )
}

private fun PersistentCompound.testGet(source: CommandSource) {
    val simpleInt = this[simpleIntKey]
    val complexClass = this[personKey]

    source.sendText {
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

private fun PersistentCompound.testRemove() {
    this -= simpleIntKey
    this -= personKey
}
