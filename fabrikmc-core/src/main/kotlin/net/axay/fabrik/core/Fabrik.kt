package net.axay.fabrik.core

import net.minecraft.server.MinecraftServer

/**
 * An object containing global values used by FabrikMC.
 */
object Fabrik {
    /**
     * The current [MinecraftServer] server instance.
     */
    var currentServer: MinecraftServer? = null
        internal set
}
