package net.axay.fabrik.igui

import net.axay.fabrik.core.text.literalText
import net.axay.fabrik.core.text.sendText
import net.axay.fabrik.igui.elements.GuiButton
import net.axay.fabrik.igui.elements.GuiButtonPageChange
import net.axay.fabrik.igui.elements.GuiFreeSlot
import net.axay.fabrik.igui.elements.GuiPlaceholder
import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.events.GuiCloseEvent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import java.time.Instant
import kotlin.random.Random

inline fun igui(
    type: GuiType,
    title: String,
    defaultPage: String = "0",
    builder: GuiBuilder.() -> Unit,
) = GuiBuilder(type, title, defaultPage).apply(builder).internalBuilder.internalBuild()

class GuiBuilder(
    val type: GuiType,
    val title: String,
    val defaultPage: String,
) {
    inner class Internal {
        val random by lazy { Random(1) }

        val pagesByKey = HashMap<String, GuiPage>()
        val pagesByNumber = HashMap<Int, GuiPage>()

        var eventHandler: GuiEventHandler? = null

        fun internalBuild() = Gui(
            type,
            title,
            pagesByKey,
            pagesByNumber,
            defaultPage,
            eventHandler ?: GuiEventHandler(null, null)
        )
    }

    /**
     * INTERNAL! You probably do not need this value.
     */
    val internalBuilder = this.Internal()

    inner class PageBuilder {
        inner class Internal {
            val content = HashMap<Int, GuiElement>()

            /**
             * Builds the page.
             *
             * INTERNAL! You probably do not need this function.
             */
            fun internalBuild(key: String, number: Int) = GuiPage(key, number, content, effectTo, effectFrom)
        }

        /**
         * INTERNAL! You probably do not need this value.
         */
        val internalBuilder = this.Internal()

        /**
         * Effect used for transitions to this page.
         * If this is not null, it will always be used even
         * if `effectFrom` is not null aswell.
         */
        var effectTo: GuiPage.ChangeEffect? = null

        /**
         * Effect used for transitions from this page. If this
         * is not null and `effectTo` is null, this will be used
         * as a fallback.
         */
        var effectFrom: GuiPage.ChangeEffect? = null

        /**
         * Adds the given element for each given slot to the gui.
         */
        fun element(guiSlotCompound: GuiSlotCompound, element: GuiElement) {
            guiSlotCompound.withDimensions(type.dimensions).mapNotNull { it.slotIndexIn(type.dimensions) }
                .forEach { internalBuilder.content[it] = element }
        }

        /**
         * Adds a button. A button has custom onClick logic.
         */
        fun button(slots: GuiSlotCompound, icon: ItemStack, onClick: (GuiClickEvent) -> Unit) =
            element(slots, GuiButton(icon, onClick))

        /**
         * Adds a placeholder. A placeholder ignores any click actions.
         */
        fun placeholder(slots: GuiSlotCompound, icon: ItemStack) =
            element(slots, GuiPlaceholder(icon))

        /**
         * Adds a free slot. A free slot allows player interaction.
         */
        fun freeSlot(slots: GuiSlotCompound, onClick: ((GuiClickEvent) -> Unit)? = null) =
            element(slots, GuiFreeSlot(onClick))

        /**
         * Adds a page change button, which will open the previous page when clicked.
         */
        fun previousPage(
            slots: GuiSlotCompound,
            icon: ItemStack,
            shouldChange: ((GuiClickEvent) -> Boolean) = { true },
            onChange: ((GuiClickEvent) -> Unit)? = null
        ) {
            element(slots, GuiButtonPageChange(
                icon, GuiButtonPageChange.Calculator.PreviousPage, shouldChange, onChange
            ))
        }

        /**
         * Adds a page change button, which will open the next page when clicked.
         */
        fun nextPage(
            slots: GuiSlotCompound,
            icon: ItemStack,
            shouldChange: ((GuiClickEvent) -> Boolean) = { true },
            onChange: ((GuiClickEvent) -> Unit)? = null
        ) {
            element(slots, GuiButtonPageChange(
                icon, GuiButtonPageChange.Calculator.NextPage, shouldChange, onChange
            ))
        }

        /**
         * Adds a page change button, which will open the specified page when clicked.
         */
        fun changePageByNumber(
            slots: GuiSlotCompound,
            icon: ItemStack,
            pageNumber: Int,
            shouldChange: ((GuiClickEvent) -> Boolean) = { true },
            onChange: ((GuiClickEvent) -> Unit)? = null
        ) {
            element(slots, GuiButtonPageChange(
                icon, GuiButtonPageChange.Calculator.StaticPageNumber(pageNumber), shouldChange, onChange
            ))
        }

        /**
         * Adds a page change button, which will open the specified page when clicked.
         */
        fun changePageByKey(
            slots: GuiSlotCompound,
            icon: ItemStack,
            pageKey: Any,
            shouldChange: ((GuiClickEvent) -> Boolean) = { true },
            onChange: ((GuiClickEvent) -> Unit)? = null
        ) {
            element(slots, GuiButtonPageChange(
                icon, GuiButtonPageChange.Calculator.StaticPageKey(pageKey), shouldChange, onChange
            ))
        }
    }

    /**
     * Add a new page to the gui.
     *
     * @param key the unique key of the page
     */
    inline fun page(
        key: Any = "${Instant.now()}${internalBuilder.random.nextInt(10, 20)}",
        number: Int = internalBuilder.pagesByNumber.keys.maxOrNull()?.plus(1) ?: 0,
        builder: PageBuilder.() -> Unit,
    ) {
        if (internalBuilder.pagesByKey.containsKey(key)) error("The specified page key already exists")
        if (internalBuilder.pagesByNumber.containsKey(key)) error("The specified page number is already in use")

        val stringKey = key.toString()
        val page = PageBuilder().apply(builder).internalBuilder.internalBuild(stringKey, number)
        internalBuilder.pagesByKey[stringKey] = page
        internalBuilder.pagesByNumber[number] = page
    }

    class EventHandlerBuilder {
        private var onClick: ((GuiClickEvent) -> Unit)? = null
        private var onClose: ((GuiCloseEvent) -> Unit)? = null

        /**
         * And event callback which will be invoked if a player
         * interacts with the inventory.
         */
        fun onClick(onClick: (GuiClickEvent) -> Unit) {
            this.onClick = onClick
        }

        /**
         * An event callback which will be invoked if the gui
         * inventory gets closed.
         */
        fun onClose(onClose: (GuiCloseEvent) -> Unit) {
            this.onClose = onClose
        }

        /**
         * Builds the event handler.
         *
         * INTERNAL! You probably do not need this function.
         */
        fun internalBuild() = GuiEventHandler(onClick, onClose)
    }

    inline fun events(builder: EventHandlerBuilder.() -> Unit) {
        internalBuilder.eventHandler = EventHandlerBuilder().apply(builder).internalBuild()
    }
}

fun main() {

    println()

    igui(GuiType.NINE_BY_ONE, "Gui") {
        events {
            onClick {
                it.player.sendText(literalText("Geklickt! Slot: ${it.slot}"))
            }
            onClose {
                it.player.sendText(literalText("Geschlossen :(") { color = 0xFF387B })
            }
        }

        page("buttonpage") {
            effectTo = GuiPage.ChangeEffect.SWIPE_HORIZONTALLY

            button(1 sl 3, Items.COBBLESTONE.defaultStack) {
                it.player.sendText(literalText("Der beste Button!"))
            }

            freeSlot(1 sl 4) {

            }
        }
    }

}
