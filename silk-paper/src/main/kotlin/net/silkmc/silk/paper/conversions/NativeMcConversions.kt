package net.silkmc.silk.paper.conversions

import net.minecraft.server.MinecraftServer
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Server
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
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

/**
 * Converts a Paper [Entity] to a native [net.minecraft.world.entity.Entity]. The instance should be a [ServerEntity], but this api does not guarantee that.
 */
val Entity.mcEntity: net.minecraft.world.entity.Entity
    get() = (this as CraftEntity).handle

@Suppress("UnstableApiUsage")
    /**
     * Converts a Paper [DamageSource] to a native [net.minecraft.world.damagesource.DamageSource].
     */
val DamageSource.mcDamageSource: net.minecraft.world.damagesource.DamageSource
    get() = (this as CraftDamageSource).handle