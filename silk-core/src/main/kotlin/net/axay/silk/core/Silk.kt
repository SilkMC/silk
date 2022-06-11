package net.axay.silk.core

import net.axay.silk.core.annotations.DelicateSilkApi
import net.minecraft.server.MinecraftServer

@Deprecated(
    message = "FabrikMC has been renamed to Silk.",
    replaceWith = ReplaceWith("net.axay.silk.core.Silk")
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
