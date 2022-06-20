package net.silkmc.silk.core

import net.minecraft.server.MinecraftServer
import net.silkmc.silk.core.annotations.DelicateSilkApi

@Deprecated(
    message = "FabrikMC has been renamed to Silk.",
    replaceWith = ReplaceWith("net.silkmc.silk.core.Silk")
)
typealias Fabrik = Silk

/**
 * An object containing global values used by Silk.
 */
object Silk {

    /**
     * The current [MinecraftServer] server instance.
     */
    @DelicateSilkApi
    var currentServer: MinecraftServer? = null
        internal set
}
