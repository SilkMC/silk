package net.silkmc.silk.commands.test

import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.silkmc.silk.commands.RegistrableCommand
import net.silkmc.silk.commands.command

class CommandStructureTest : FunSpec({
    context("built command should have correct structure") {
        context("scoped declaration style") {
            context("simple command") {
                test("command without subcommands") {
                    val command = command("testcommand") {
                        runs { doNothing() }
                    }.extract()
                    printCommand(command) shouldBe """
                        -> testcommand
                         |- has executor
                    """.trimIndent()
                }

                test("command with subcommands") {
                    val command = command("testcommand") {
                        literal("subcommandone") {
                            runs { doNothing() }
                        }
                        literal("subcommandtwo") {
                            runs { doNothing() }
                        }
                    }.extract()
                    printCommand(command) shouldBe """
                        -> testcommand
                          -> subcommandone
                           |- has executor
                          -> subcommandtwo
                           |- has executor
                    """.trimIndent()
                }
            }

            test("more complex command with an argument") {
                val command = command("testcommand") {
                    literal("subcommandone") {
                        runs { doNothing() }

                        argument<String>("argumentone") {
                            suggestList { emptyList() }
                            runs { doNothing() }
                        }
                    }
                    literal("subcommandtwo") {
                        runs { doNothing() }
                    }
                }.extract()
                printCommand(command) shouldBe """
                    -> testcommand
                      -> subcommandone
                       |- has executor
                        -> argumentone
                         |- has executor
                         |- argument type of StringArgumentType
                         |- has suggestions
                      -> subcommandtwo
                       |- has executor
                """.trimIndent()
            }
        }

        test("short declaration style") {
            val command = command("testcommand") {
                literal("subcommandone") runs { doNothing() }
                literal("subcommandtwo") runs { doNothing() }
            }.extract()
            printCommand(command) shouldBe """
                -> testcommand
                  -> subcommandone
                   |- has executor
                  -> subcommandtwo
                   |- has executor
            """.trimIndent()
        }
    }
})

fun RegistrableCommand<CommandSourceStack>.extract() =
    commandBuilder.toBrigadier(mockk()).first()

fun <S : SharedSuggestionProvider> printCommand(commandNode: CommandNode<S>, depth: Int = 0, stringBuilder: StringBuilder = StringBuilder()): String {
    fun printWithDepth(message: Any) = println((" ".repeat(depth * 2) + message).also { stringBuilder.appendLine(it) })

    printWithDepth("-> ${commandNode.name}")
    commandNode.command?.let { printWithDepth(" |- has executor") }
    if (commandNode is ArgumentCommandNode<S, *>) {
        commandNode.customSuggestions?.let { printWithDepth(" |- argument type of ${commandNode.type::class.simpleName}") }
        commandNode.customSuggestions?.let { printWithDepth(" |- has suggestions") }
    }

    commandNode.children.forEach {
        printCommand(it, depth + 1, stringBuilder)
    }

    return stringBuilder.toString().trim()
}

fun doNothing() = Unit
