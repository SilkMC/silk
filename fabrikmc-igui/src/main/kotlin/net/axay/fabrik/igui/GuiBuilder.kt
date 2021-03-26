package net.axay.fabrik.igui

import net.axay.fabrik.igui.elements.*
import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.events.GuiCloseEvent
import net.axay.fabrik.igui.observable.GuiList
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import java.time.Instant
import kotlin.random.Random

/**
 * Creates a new gui.
 *
 * @param type the type of the gui, which specifies the dimensions
 * of the gui
 * @param title the title of the gui, displayed at the top
 * @param defaultPageKey the default page, which will be loaded
 * on initialization
 * @param builder the gui builder
 */
inline fun igui(
    type: GuiType,
    title: LiteralText,
    defaultPageKey: Any,
    builder: GuiBuilder.() -> Unit,
) = GuiBuilder(type, title, defaultPageKey).apply(builder).internalBuilder.internalBuild()

class GuiBuilder(
    val type: GuiType,
    val title: LiteralText,
    val defaultPageKey: Any,
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
            defaultPageKey.toString(),
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
         * Sets both [effectTo] and [effectFrom] at the same time.
         */
        fun setEffect(effect: GuiPage.ChangeEffect?) {
            effectTo = effect
            effectFrom = effect
        }

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
        fun button(slots: GuiSlotCompound, icon: GuiIcon, onClick: (GuiClickEvent) -> Unit) =
            element(slots, GuiButton(icon, onClick))

        /**
         * Adds a placeholder. A placeholder ignores any click actions.
         */
        fun placeholder(slots: GuiSlotCompound, icon: GuiIcon) =
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
            icon: GuiIcon,
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
            icon: GuiIcon,
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
            icon: GuiIcon,
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
            icon: GuiIcon,
            pageKey: Any,
            shouldChange: ((GuiClickEvent) -> Boolean) = { true },
            onChange: ((GuiClickEvent) -> Unit)? = null
        ) {
            element(slots, GuiButtonPageChange(
                icon, GuiButtonPageChange.Calculator.StaticPageKey(pageKey), shouldChange, onChange
            ))
        }

        /**
         * Creates a new rectangular compound (startSlot and endSlot define
         * the corners of the compound).
         *
         * @return the compound, which is needed for other elements, like
         * a compound scroll button
         */
        fun <E> compound(
            slots: GuiSlotCompound.SlotRange.Rectangle,
            content: GuiList<E>,
            iconGenerator: (E) -> ItemStack,
            onClick: ((event: GuiClickEvent, element: E) -> Unit)? = null,
        ): GuiCompound<E> {
            val compound = GuiCompound(type, slots, content, iconGenerator, onClick)
            element(slots, GuiCompoundElement(compound))
            return compound
        }

        /**
         * Adds a compound scroll button.
         *
         * Used by both [compoundScrollForwards] and [compoundScrollBackwards],
         * which are easier to use than this function.
         */
        fun compoundScroll(
            slots: GuiSlotCompound,
            icon: GuiIcon,
            compound: GuiCompound<*>,
            reverse: Boolean,
            speed: Int = 50,
            scrollDistance: Int = compound.compoundWidth,
            scrollTimes: Int = compound.compoundHeight,
        ) {
            element(slots, GuiButtonCompoundScroll(
                icon, compound, reverse, speed.toLong(), scrollDistance, scrollTimes
            ))
        }

        /**
         * Adds a compound scroll button.
         *
         * This one scrolls forwards, line by line.
         */
        fun compoundScrollForwards(
            slots: GuiSlotCompound,
            icon: GuiIcon,
            compound: GuiCompound<*>,
            speed: Int = 50,
            scrollTimes: Int = compound.compoundHeight,
        ) {
            element(slots, GuiButtonCompoundScroll(
                icon, compound, false, speed.toLong(), compound.compoundWidth, scrollTimes
            ))
        }

        /**
         * Adds a compound scroll button.
         *
         * This one scrolls backwards, line by line.
         */
        fun compoundScrollBackwards(
            slots: GuiSlotCompound,
            icon: GuiIcon,
            compound: GuiCompound<*>,
            speed: Int = 50,
            scrollTimes: Int = compound.compoundHeight,
        ) {
            element(slots, GuiButtonCompoundScroll(
                icon, compound, true, speed.toLong(), compound.compoundWidth, scrollTimes
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

    /**
     * Opens a new [EventHandlerBuilder] to build and set a new
     * [GuiEventHandler] for the gui.
     */
    inline fun events(builder: EventHandlerBuilder.() -> Unit) {
        internalBuilder.eventHandler = EventHandlerBuilder().apply(builder).internalBuild()
    }
}
