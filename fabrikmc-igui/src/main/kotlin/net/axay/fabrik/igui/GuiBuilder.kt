@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.axay.fabrik.igui

import net.axay.fabrik.igui.elements.*
import net.minecraft.item.ItemStack
import kotlin.math.absoluteValue

fun igui(
    type: GuiType,
    guiCreator: GuiCreator = IndividualGuiCreator,
    builder: GuiBuilder.() -> Unit,
) = GuiBuilder(type, guiCreator).apply(builder).build()

class GuiBuilder(
    val type: GuiType,
    private val guiCreator: GuiCreator
) {

    /**
     * The title of this Gui.
     * This title will be visible for every page of
     * this Gui.
     */
    var title: String = ""

    /**
     * The transition applied, if another Gui redirects to
     * this Gui.
     */
    var transitionTo: InventoryChangeEffect? = null

    /**
     * The transition applied, if this Gui redirects to
     * another Gui and the other Gui has no transitionTo
     * value defined.
     */
    var transitionFrom: InventoryChangeEffect? = null

    /**
     * The default page will be loaded first for every
     * Gui instance.
     */
    var defaultPage = 1

    private val guiSlots = HashMap<Int, GuiPage>()

    private var onClickElement: ((GuiClickEvent) -> Unit)? = null

    /**
     * Opens the builder for a new page and adds
     * the new page to the Gui.
     * @param page The index of the page.
     */
    fun page(page: Int, builder: GuiPageBuilder.() -> Unit) {
        guiSlots[page] = GuiPageBuilder(type, page).apply(builder).build()
    }

    /**
     * A callback executed when the user clicks on
     * any Gui elements on any page in this Gui.
     */
    fun onClickElement(onClick: (GuiClickEvent) -> Unit) {
        onClickElement = onClick
    }

    internal fun build() = guiCreator.createInstance(
        GuiData(type, title, guiSlots, defaultPage, transitionTo, transitionFrom, onClickElement)
    )

}

class GuiPageBuilder(
    private val type: GuiType,
    val page: Int
) {

    private val guiSlots = HashMap<Int, GuiSlot>()

    var transitionTo: PageChangeEffect? = null
    var transitionFrom: PageChangeEffect? = null

    internal fun build() = GuiPage(page, guiSlots, transitionTo, transitionFrom)

    private fun defineSlots(slots: InventorySlotCompound, element: GuiSlot) =
        slots.withInvType(type).forEach { curSlot ->
            curSlot.realSlotIn(type.dimensions)?.let { guiSlots[it] = element }
        }

    /**
     * A button is an item protected from any player
     * actions. If clicked, the specified [onClick]
     * function is invoked.
     */
    fun button(slots: InventorySlotCompound, itemStack: ItemStack, onClick: (GuiClickEvent) -> Unit) =
        defineSlots(slots, GuiButton(itemStack, onClick))

    /**
     * An item protected from any player actions.
     * This is not a button.
     */
    fun placeholder(slots: InventorySlotCompound, itemStack: ItemStack) =
        defineSlots(slots, GuiPlaceholder(itemStack))

    /**
     * A free slot does not block any player actions.
     * The player can put items in this slot or take
     * items out of it.
     */
    fun freeSlot(slots: InventorySlotCompound) = defineSlots(slots, GuiFreeSlot())

    /**
     * This is a button which loads the specified
     * [toPage] if clicked.
     */
    fun pageChanger(
        slots: InventorySlotCompound,
        icon: ItemStack,
        toPage: Int,
        onChange: ((GuiClickEvent) -> Unit)? = null
    ) = defineSlots(
        slots, GuiButtonPageChange(
            icon,
            GuiPageChangeCalculator.GuiConsistentPageCalculator(toPage),
            onChange
        )
    )

    /**
     * This button always tries to find the previous
     * page if clicked, and if a previous page
     * exists it is loaded.
     */
    fun previousPage(
        slots: InventorySlotCompound,
        icon: ItemStack,
        onChange: ((GuiClickEvent) -> Unit)? = null
    ) = defineSlots(
        slots, GuiButtonPageChange(
            icon,
            GuiPageChangeCalculator.GuiPreviousPageCalculator,
            onChange
        )
    )

    /**
     * This button always tries to find the next
     * page if clicked, and if a next page
     * exists it is loaded.
     */
    fun nextPage(
        slots: InventorySlotCompound,
        icon: ItemStack,
        onChange: ((GuiClickEvent) -> Unit)? = null
    ) = defineSlots(
        slots, GuiButtonPageChange(
            icon,
            GuiPageChangeCalculator.GuiNextPageCalculator,
            onChange
        )
    )

    /**
     * By pressing this button, the player switches to another
     * Gui. The transition effect is applied.
     */
    fun changeGui(
        slots: InventorySlotCompound,
        icon: ItemStack,
        newGui: () -> Gui,
        newPage: Int? = null,
        onChange: ((GuiClickEvent) -> Unit)? = null
    ) = defineSlots(
        slots, GuiButtonInventoryChange(
            icon,
            newGui,
            newPage,
            onChange
        )
    )

    /**
     * Creates a new compound, holding simple compound elements.
     */
    fun createSimpleCompound() = createCompound<GuiCompoundElement>(
        iconGenerator = { it.icon },
        onClick = { clickEvent, element -> element.onClick?.invoke(clickEvent) }
    )

    /**
     * Creates a new compound, holding data which can be displayed
     * in any compound space.
     */
    fun <E> createCompound(
        iconGenerator: (E) -> ItemStack,
        onClick: ((clickEvent: GuiClickEvent, element: E) -> Unit)? = null
    ) = GuiSpaceCompound(type, iconGenerator, onClick)

    /**
     * Defines an area where the content of the given compound
     * is displayed.
     */
    fun <E> compoundSpace(
        slots: InventorySlotCompound,
        compound: GuiSpaceCompound<E>
    ) {
        compound.addSlots(slots)
        defineSlots(
            slots,
            GuiSpaceCompoundElement(compound)
        )
    }

    /**
     * Creates a new compound, holding simple compound elements.
     * This compound is strictly a rectangle.
     * The space is automatically defined.
     *
     * This method sets the element type to
     * [GuiCompoundElement]. The iconGenerator and onClick callback
     * are automatically defined.
     */
    fun createSimpleRectCompound(
        fromSlot: SingleInventorySlot,
        toSlot: SingleInventorySlot
    ) = createRectCompound<GuiCompoundElement>(

        fromSlot, toSlot,

        iconGenerator = { it.icon },
        onClick = { clickEvent, element -> element.onClick?.invoke(clickEvent) }

    )

    /**
     * Creates a new compound, holding custom element data.
     * This compound is strictly a rectangle.
     * The space is automatically defined.
     */
    fun <E> createRectCompound(
        fromSlot: SingleInventorySlot,
        toSlot: SingleInventorySlot,
        iconGenerator: (E) -> ItemStack,
        onClick: ((clickEvent: GuiClickEvent, element: E) -> Unit)? = null
    ): GuiRectSpaceCompound<E> {
        val rectSlotCompound = fromSlot rectTo toSlot
        return GuiRectSpaceCompound(
            type,
            iconGenerator,
            onClick,
            (rectSlotCompound.endInclusive.slotInRow - rectSlotCompound.start.slotInRow) + 1
        ).apply {
            addSlots(rectSlotCompound)
            defineSlots(
                rectSlotCompound,
                GuiSpaceCompoundElement(this)
            )
        }
    }

    /**
     * By pressing this button,
     * the user scrolls forwards or backwards in the compound.
     */
    fun compoundScroll(
        slots: InventorySlotCompound,
        icon: ItemStack,
        compound: GuiSpaceCompound<*>,
        scrollDistance: Int = 1,
        scrollTimes: Int = 1,
        reverse: Boolean = false
    ) = defineSlots(
        slots,
        GuiSpaceCompoundScrollButton(icon, compound, scrollDistance.absoluteValue, scrollTimes, reverse)
    )

    /**
     * By pressing this button,
     * the user scrolls forwards or backwards in the compound.
     */
    fun compoundScroll(
        slots: InventorySlotCompound,
        icon: ItemStack,
        compound: GuiRectSpaceCompound<*>,
        scrollTimes: Int = 1,
        reverse: Boolean = false
    ) = defineSlots(
        slots,
        GuiSpaceCompoundScrollButton(icon, compound, scrollTimes, reverse)
    )

}
