package net.silkmc.silk.compose.mixin;

import net.minecraft.world.level.material.MaterialColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MaterialColor.class)
public interface MaterialColorAccessor {
    @Accessor("MATERIAL_COLORS")
    static MaterialColor[] getMaterialColors() {
        throw new AssertionError();
    }
}
