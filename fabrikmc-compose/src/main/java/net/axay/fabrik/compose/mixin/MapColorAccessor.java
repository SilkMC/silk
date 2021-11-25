package net.axay.fabrik.compose.mixin;

import net.minecraft.block.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MapColor.class)
public interface MapColorAccessor {
    @Accessor("COLORS")
    static MapColor[] getColors() {
        throw new AssertionError();
    }
}
