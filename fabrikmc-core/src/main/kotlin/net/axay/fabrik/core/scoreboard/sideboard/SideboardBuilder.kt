package net.axay.fabrik.core.scoreboard.sideboard

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.text.LiteralTextBuilder
import net.axay.fabrik.core.text.literalText
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

/**
 * Displays the given [sideboard] to the player.
 * Future updates will be visible to the player as well.
 *
 * If the player leaves and then reconnects, you have to call this function again
 * if you wish to show the player the same sideboard.
 */
fun ServerPlayerEntity.showSideboard(sideboard: Sideboard) {
    sideboard.displayToPlayer(this)
}

/**
 * Opens a new sideboard [builder] and returns the final sideboard.
 *
 * You can then open the sideboard to a [ServerPlayerEntity] using
 * [ServerPlayerEntity.showSideboard].
 *
 * @param displayName the name of sideboard, which is displayed on the top
 * @param name an optional internal name of the sideboard (only visible to the player
 * via commands) - this defaults to the string of [displayName]
 * @param builder the [SideboardBuilder]
 *
 * @return the final instance of [Sideboard]
 */
inline fun sideboard(
    displayName: Text,
    name: String = displayName.string.filter { it.isLetter() }.take(16),
    builder: SideboardBuilder.() -> Unit
) = Sideboard(name, displayName, SideboardBuilder().apply(builder).lines)

/**
 * A helper class which is used to create a [Sideboard].
 *
 * You probably want to use this class via the [sideboard] function.
 */
class SideboardBuilder {
    @PublishedApi
    internal val lines = ArrayList<SideboardLine>()

    /**
     * Adds a simple and static line of text.
     */
    fun line(text: Text) {
        lines += SimpleSideboardLine(text)
    }

    /**
     * Adds a simple and static line of text.
     * The [block] parameter can be used to add some additional logic.
     */
    inline fun line(block: () -> Text) {
        lines += SimpleSideboardLine(block())
    }

    /**
     * Adds a line where the content is changing.
     *
     * To change the content, simply emit a new value to the flow.
     *
     * This function allows you to add some custom change logic. If you
     * want your line to update / change periodically, use the
     * [lineChangingPeriodically] or [literalLineChangingPeriodically]
     * functions instead.
     *
     * See [flow] documention to learn more about flows.
     */
    inline fun lineChanging(crossinline flowBuilder: suspend FlowCollector<Text>.() -> Unit) {
        lines += ChangingSideboardLine(flow { this.flowBuilder() })
    }

    /**
     * Adds a line where the content is changing / updating periodically.
     *
     * @param period the period in milliseconds
     * @param block the callback which is executed each time to get the content
     * of the line
     */
    inline fun lineChangingPeriodically(period: Long, crossinline block: suspend () -> Text) {
        lines += ChangingSideboardLine(flow {
            while (Fabrik.currentServer?.isRunning == true) {
                emit(block())
                delay(period)
            }
        })
    }

    /**
     * Adds a simple and static line of text.
     *
     * The difference to [line] is that this function immediately opens a
     * [literalText] builder.
     */
    inline fun literalLine(baseText: String = "", crossinline builder: LiteralTextBuilder.() -> Unit = {}) {
        lines += SimpleSideboardLine(literalText(baseText, builder))
    }

    /**
     * A utility function, allowing you to emit a [Text] to the flow, which is
     * built by an easy to use [literalText] builder.
     */
    suspend inline fun FlowCollector<Text>.emitLiteralText(
        baseText: String = "",
        crossinline builder: LiteralTextBuilder.() -> Unit = {}
    ) = emit(literalText(baseText, builder))
}
