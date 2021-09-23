package net.axay.fabrik.game.sideboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import net.minecraft.text.Text

/**
 * This interface represents one line in a [Sideboard].
 */
interface SideboardLine {
    val textFlow: Flow<Text>
}

/**
 * A sideboard line which does not change and always displays the
 * same [Text].
 */
class SimpleSideboardLine(text: Text) : SideboardLine {
    override val textFlow = flowOf(text)
}

/**
 * A sideboard line which does change. Everytime the given [textFlow] emits
 * a new [Text], the sideboard line will be updated for all players currently
 * seeing the [Sideboard].
 */
class ChangingSideboardLine(override val textFlow: Flow<Text>) : SideboardLine
