package net.axay.fabrik.igui

import net.axay.fabrik.igui.DslAnnotations.EventLevel.GuiEventDsl
import net.axay.fabrik.igui.DslAnnotations.PageLevel.GuiCompoundDsl
import net.axay.fabrik.igui.DslAnnotations.PageLevel.GuiPageDsl
import net.axay.fabrik.igui.DslAnnotations.TopLevel.GuiDsl
import net.axay.fabrik.igui.elements.*
import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.events.GuiCloseEvent
import net.axay.fabrik.igui.observable.GuiList
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import java.util.*
import kotlin.random.Random

private class DslAnnotations {
    class TopLevel {
        @DslMarker
        annotation class GuiDsl
    }

    class PageLevel {
        @DslMarker
        annotation class GuiPageDsl

        @DslMarker
        annotation class GuiCompoundDsl
    }

    class EventLevel {
        @DslMarker
        annotation class GuiEventDsl
    }
}

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
) = GuiBuilder(type, title, defaultPageKey).apply(builder).build()

@GuiDsl
class GuiBuilder(
    val type: GuiType,
    val title: LiteralText,
    val defaultPageKey: Any,
) {
    @PublishedApi
    internal val random by lazy { Random(1) }

    @PublishedApi
    internal val pagesByKey = HashMap<String, GuiPage>()
    @PublishedApi
    internal val pagesByNumber = HashMap<Int, GuiPage>()

    @PublishedApi
    internal var eventHandler: GuiEventHandler? = null

    @GuiDsl
    inner class PageBuilder(
        val key: String,
        val number: Int,
    ) {
        private val content = HashMap<Int, GuiElement>()

        /**
         * Effect used for transitions to this page.
         * If this is not null, it will always be used even
         * if `effectFrom` is not null aswell.
         */
        @GuiPageDsl
        var effectTo: GuiPage.ChangeEffect? = null

        /**
         * Effect used for transitions from this page. If this
         * is not null and `effectTo` is null, this will be used
         * as a fallback.
         */
        @GuiPageDsl
        var effectFrom: GuiPage.ChangeEffect? = null

        /**
         * Sets both [effectTo] and [effectFrom] at the same time.
         */
        @GuiPageDsl
        fun setEffect(effect: GuiPage.ChangeEffect?) {
            effectTo = effect
            effectFrom = effect
        }

        /**
         * Adds the given element for each given slot to the gui.
         */
        @GuiPageDsl
        fun element(guiSlotCompound: GuiSlotCompound, element: GuiElement) {
            guiSlotCompound.withDimensions(this@GuiBuilder.type.dimensions).mapNotNull { it.slotIndexIn(this@GuiBuilder.type.dimensions) }
                .forEach { content[it] = element }
        }

        /**
         * Adds a button. A button has custom onClick logic.
         */
        @GuiPageDsl
        fun button(slots: GuiSlotCompound, icon: GuiIcon, onClick: suspend (GuiClickEvent) -> Unit) =
            element(slots, GuiButton(icon, onClick))

        /**
         * Adds a placeholder. A placeholder ignores any click actions.
         */
        @GuiPageDsl
        fun placeholder(slots: GuiSlotCompound, icon: GuiIcon) =
            element(slots, GuiPlaceholder(icon))

        /**
         * Adds a free slot. A free slot allows player interaction.
         */
        @GuiPageDsl
        fun freeSlot(slots: GuiSlotCompound, onClick: ((GuiClickEvent) -> Unit)? = null) =
            element(slots, GuiFreeSlot(onClick))

        /**
         * Adds a page change button, which will open the previous page when clicked.
         */
        @GuiPageDsl
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
        @GuiPageDsl
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
        @GuiPageDsl
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
        @GuiPageDsl
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
        @GuiCompoundDsl
        fun <E> compound(
            slots: GuiSlotCompound.SlotRange.Rectangle,
            content: GuiList<E>,
            iconGenerator: (E) -> ItemStack,
            onClick: (suspend (event: GuiClickEvent, element: E) -> Unit)? = null,
        ): GuiCompound<E> {
            val compound = GuiCompound(this@GuiBuilder.type, slots, content, iconGenerator, onClick)
            element(slots, GuiCompoundElement(compound))
            return compound
        }

        /**
         * Adds a compound scroll button.
         *
         * Used by both [compoundScrollForwards] and [compoundScrollBackwards],
         * which are easier to use than this function.
         */
        @GuiCompoundDsl
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
        @GuiCompoundDsl
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
        @GuiCompoundDsl
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

        @PublishedApi
        internal fun build() = GuiPage(key, number, content, effectTo, effectFrom)
    }

    /**
     * Add a new page to the gui.
     *
     * @param key the unique key of the page
     */
    @GuiDsl
    inline fun page(
        key: Any = UUID.randomUUID(),
        number: Int = pagesByNumber.keys.maxOrNull()?.plus(1) ?: 0,
        builder: PageBuilder.() -> Unit,
    ) {
        val stringKey = key.toString()

        if (pagesByKey.containsKey(stringKey)) error("The specified page key already exists")
        if (pagesByNumber.containsKey(number)) error("The specified page number is already in use")

        val page = PageBuilder(stringKey, number).apply(builder).build()
        pagesByKey[stringKey] = page
        pagesByNumber[number] = page
    }

    @GuiDsl
    class EventHandlerBuilder {
        private var onClick: (suspend (GuiClickEvent) -> Unit)? = null
        private var onClose: (suspend (GuiCloseEvent) -> Unit)? = null

        /**
         * And event callback which will be invoked if a player
         * interacts with the inventory.
         */
        @GuiEventDsl
        fun onClick(onClick: suspend (GuiClickEvent) -> Unit) {
            this.onClick = onClick
        }

        /**
         * An event callback which will be invoked if the gui
         * inventory gets closed.
         */
        @GuiEventDsl
        fun onClose(onClose: suspend (GuiCloseEvent) -> Unit) {
            this.onClose = onClose
        }

        @PublishedApi
        internal fun build() = GuiEventHandler(onClick, onClose)
    }

    /**
     * Opens a new [EventHandlerBuilder] to build and set a new
     * [GuiEventHandler] for the gui.
     */
    @GuiDsl
    inline fun events(builder: EventHandlerBuilder.() -> Unit) {
        eventHandler = EventHandlerBuilder().apply(builder).build()
    }

    @PublishedApi
    internal fun build() = Gui(
        type,
        title,
        pagesByKey,
        pagesByNumber,
        defaultPageKey.toString(),
        eventHandler ?: GuiEventHandler(null, null)
    )
}
