package net.silkmc.silk.paper.conversions

import net.minecraft.server.MinecraftServer
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Server
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

/**
 * Converts a Paper [Server] to a native [MinecraftServer].
 * The instance should be a [DedicatedServer], but this api does not guarantee that.
 */
val Server.mcServer: MinecraftServer
    get() = (this as CraftServer).server

/**
 * Converts a Paper [Player] to a native [ServerPlayer].
 */
val Player.mcPlayer: ServerPlayer
    get() = (this as CraftPlayer).handle
