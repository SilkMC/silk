package net.silkmc.silk.commands.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientSuggestionProvider.class)
public interface ClientSuggestionProviderAccessor {

    @Accessor
    ClientPacketListener getConnection();

    @Accessor
    Minecraft getMinecraft();
}
