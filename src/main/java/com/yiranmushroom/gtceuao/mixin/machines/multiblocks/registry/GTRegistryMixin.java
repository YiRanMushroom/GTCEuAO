package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.google.common.collect.BiMap;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.api.registry.GTRegistry;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.units.qual.K;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(GTRegistry.class)
public abstract class GTRegistryMixin<K, V> implements Iterable<V> {
    /**
     * @author YiranMushroom
     * @reason Overwrite the register method to overwrite static final values.
     */
    @Overwrite(remap = false)
    public void register(K key, V value) {
        ((GTRegistry<K, V>) (Object) this).remove(key);
        ((GTRegistry<K, V>) (Object) this).registerOrOverride(key, value);
    }
}
