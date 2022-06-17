package net.axay.silk.core.world.pos

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt

fun Vec3.toPrettyString(): String {
    return "(${this.x.roundToInt()}, ${this.y.roundToInt()}, ${this.z.roundToInt()})"
}

fun ServerPlayer.toPrettyLocationStringWithWorld(): String {
    return "(${this.x.roundToInt()}, ${this.y.roundToInt()}, ${this.z.roundToInt()}, ${
        this.level.dimension().location().path
    })"
}