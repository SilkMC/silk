package net.axay.fabrik.commands.test

import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import io.kotest.core.spec.style.FunSpec
import net.axay.fabrik.commands.*
import net.minecraft.command.CommandSource

class CommandTest : FunSpec({
    fun <S : CommandSource> printCommand(commandNode: CommandNode<S>, depth: Int = 0) {
        fun printWithDepth(message: Any) = println(" ".repeat(depth * 2) + message)

        printWithDepth("-> ${commandNode.name}")
        commandNode.command?.let { printWithDepth(" |- has executor") }
        if (commandNode is ArgumentCommandNode<S, *>) {
            commandNode.customSuggestions?.let { printWithDepth(" |- argument type of ${commandNode.type::class.simpleName}") }
            commandNode.customSuggestions?.let { printWithDepth(" |- has suggestions") }
        }

        commandNode.children.forEach {
            printCommand(it, depth + 1)
        }
    }

    fun doNothing() = Unit

    context("built command should have correct structure") {
        context("scoped declaration style") {
            context("simple command") {
                test("command without subcommands") {
                    val command = command("testcommand") {
                        runs { doNothing() }
                    }
                    printCommand(command.build())
                }

                test("command with subcommands") {
                    val command = command("testcommand") {
                        literal("subcommandone") {
                            runs { doNothing() }
                        }
                        literal("subcommandtwo") {
                            runs { doNothing() }
                        }
                    }
                    printCommand(command.build())
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
                }
                printCommand(command.build())
            }
        }

        test("short declaration style") {
            val command = command("testcommand") {
                literal("subcommandone") runs { doNothing() }
                literal("subcommandtwo") runs { doNothing() }
            }
            printCommand(command.build())
        }
    }
})
