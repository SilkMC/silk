package net.silkmc.silk.game.tablist

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.game.scoreboard.ScoreboardLine

/**
 * Opens a new tablist [builder] and returns the final tablist.
 *
 * @param builder the [TablistBuilder]
 *
 * @return the final instance of [Tablist]
 */
inline fun tablist(
    builder: TablistBuilder.() -> Unit
): Tablist {
    val tablistBuilder = TablistBuilder().apply(builder)
    return Tablist(
        tablistBuilder.nameGenerator,
        tablistBuilder.headerLines,
        tablistBuilder.footerLines
    )
}

/**
 * A helper class which is used to create a [Tablist].
 *
 * You probably want to use this class via the [tablist] function.
 */
class TablistBuilder {

    @PublishedApi
    internal val headerLines = ArrayList<ScoreboardLine>()

    @PublishedApi
    internal val footerLines = ArrayList<ScoreboardLine>()

    @PublishedApi
    internal var nameGenerator: (suspend ServerPlayer.() -> Component)? = null

    /**
     * Adds the name generator to the [Tablist]
     *
     * @param nameGenerator the generator to get the name
     */
    fun generateName(nameGenerator: suspend ServerPlayer.() -> Component) {
        this@TablistBuilder.nameGenerator = nameGenerator
    }

    /**
     * Adds footer lines implementing the [ScoreboardLine] interface.
     *
     * @param lines the footer lines of the tablist
     */
    fun footer(lines: List<ScoreboardLine>) {
        footerLines += lines
    }

    /**
     * Adds header lines implementing the [ScoreboardLine] interface.
     *
     * @param lines the header lines of the tablist
     */
    fun header(lines: List<ScoreboardLine>) {
        headerLines += lines
    }
}