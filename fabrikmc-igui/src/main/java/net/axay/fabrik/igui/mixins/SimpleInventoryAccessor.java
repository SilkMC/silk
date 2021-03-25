package net.axay.fabrik.igui.mixins;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleInventory.class)
public interface SimpleInventoryAccessor {
    @Accessor("stacks")
    public DefaultedList<ItemStack> getStacks();
}
