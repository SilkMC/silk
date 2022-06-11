package net.axay.silk.commands

/**
 * An enum containing known permission levels. Information taken from
 * [the Minecraft wiki](https://minecraft.fandom.com/wiki/Permission_level#Java_Edition)
 */
enum class PermissionLevel(val level: Int) {
    /**
     * The default permission level.
     */
    NONE(0),

    /**
     * Players with this level can bypass spawn protection.
     */
    BYPASS_SPAWN_PROTECTION(1),

    /**
     * This level allows player to use all commands, except the ones managing other
     * players, such as `/ban` and `/kick`.
     */
    COMMAND_RIGHTS(2),

    /**
     * All previous rights plus the right to ban and kick players.
     */
    BAN_RIGHTS(3),

    /**
     * All previous rights plus the ability to stop the server.
     */
    OWNER(4),
}
