import net.axay.fabrik.core.Fabrik
import net.minecraft.world.entity.player.Player

fun Player.runCommand(command: String) {
    Fabrik.currentServer!!.commands.performCommand(this.createCommandSourceStack(), command)
}