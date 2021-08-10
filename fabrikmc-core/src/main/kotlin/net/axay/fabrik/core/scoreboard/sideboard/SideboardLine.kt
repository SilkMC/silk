package net.axay.fabrik.core.scoreboard.sideboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import net.minecraft.text.Text

interface SideboardLine {
    val textFlow: Flow<Text>
}

class SimpleSideboardLine(text: Text) : SideboardLine {
    override val textFlow = flowOf(text)
}

class ChangingSideboardLine(override val textFlow: Flow<Text>) : SideboardLine
