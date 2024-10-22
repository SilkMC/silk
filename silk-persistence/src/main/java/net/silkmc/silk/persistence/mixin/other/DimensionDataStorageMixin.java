package net.silkmc.silk.persistence.mixin.other;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.silkmc.silk.persistence.internal.EmptySilkPersistenceCompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Mixin(DimensionDataStorage.class)
public class DimensionDataStorageMixin {

    @WrapOperation(
            method = "method_61878",
            at = @At(
                    value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private <K, V> V dontSaveEmptyCompounds(Map<K, V> instance, K key, V value, Operation<V> original) {
        if (key instanceof Path path && Files.notExists(path)
                && value instanceof CompoundTag compoundTag
                && compoundTag.getCompound("data") instanceof EmptySilkPersistenceCompoundTag) {
            return null;
        }

        return original.call(instance, key, value);
    }
}
