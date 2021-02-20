package net.axay.fabrik.test

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.igui.*
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource

fun init() {

    Fabrik.init()

    GuiCommand

}

object GuiCommand : ModCommand() {

    override val command: LiteralArgumentBuilder<ServerCommandSource> = literal("gui")
        .then(
            literal("open").executes {
                println("Trying to open gui :poggles:")

                try {
                    val gui = igui(GuiType.NINE_BY_THREE) {
                        title = "Moincraft"
                        defaultPage = 1

                        page(1) {
                            transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY

                            placeholder(Slots.All, ItemStack(Items.WHITE_STAINED_GLASS_PANE))

                            nextPage(Slots.ColumnNine, ItemStack(Items.PAPER))
                        }

                        page(2) {
                            transitionFrom = PageChangeEffect.SWIPE_VERTICALLY

                            placeholder(Slots.All, ItemStack(Items.BLACK_STAINED_GLASS_PANE))

                            previousPage(Slots.ColumnOne, ItemStack(Items.PAPER))
                        }
                    }

                    it.source.player.openGui(gui)
                } catch (exc: Throwable) {
                    exc.printStackTrace()
                }

                return@executes 1
            }
        )

}