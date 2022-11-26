package net.silkmc.silk.core.serialization.serializers

import com.mojang.math.Vector3d
import com.mojang.math.Vector3f
import net.minecraft.core.Vec3i
import net.silkmc.silk.core.serialization.SilkDelegateSerializer
import net.silkmc.silk.core.world.pos.Pos3d
import net.silkmc.silk.core.world.pos.Pos3f
import net.silkmc.silk.core.world.pos.Pos3i

object Vec3iSerializer :
    SilkDelegateSerializer<Vec3i, Pos3i>(Pos3i.serializer(), ::Pos3i, Pos3i::toMcVec3i)

object Vector3fSerializer :
    SilkDelegateSerializer<Vector3f, Pos3f>(Pos3f.serializer(), ::Pos3f, Pos3f::toMcVector3f)

object Vector3dSerializer :
    SilkDelegateSerializer<Vector3d, Pos3d>(Pos3d.serializer(), ::Pos3d, Pos3d::toMcVector3d)
