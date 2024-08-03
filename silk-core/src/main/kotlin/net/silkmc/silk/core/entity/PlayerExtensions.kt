package net.silkmc.silk.core.entity

import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.silkmc.silk.core.server.executeCommand

/**
 * Executes the given [command] for this player.
 *
 * Note that the [command] must not contain the slash prefix.
 */
fun Player.executeCommand(command: String) {
    val serverPlayer = this as? ServerPlayer
    serverPlayer?.server?.executeCommand(command, serverPlayer.createCommandSourceStack())
}

/**
 * Checks if a player has a given permission.
 *
 * If
 * [Fabric Permissions API](https://github.com/lucko/fabric-permissions-api/)
 * is not installed, then it will fall back to the permission level and
 * test it against the player's permission level.
 *
 * Operators have a permission level of 4, normal users have a permission
 * level of 0. It is unclear what exactly 1-3 mean.
 *
 * @param permission The permission to check
 * @param fallbackPermissionLevel The permission level to fall back to if
 *    the fabric permissions api is not present
 * @return If the player has the appropriate permissions
 */
fun ServerPlayer.hasPermission(permission: String, fallbackPermissionLevel: Int = 2): Boolean {
    return if (FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0"))
        Permissions.check(this, permission, fallbackPermissionLevel)
    else
        hasPermissions(fallbackPermissionLevel)
}
