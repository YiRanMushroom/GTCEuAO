package com.yiranmushroom.gtceuao.mixin.misc.bedrockDrills;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreVeinSavedData;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BedrockOreVeinSavedData.class)
public class BedrockOreVeinSavedDataMixin {
    @Inject(method = "depleteVein", at = @At("HEAD"), cancellable = true, remap = false)
    private void depleteVein(int chunkX, int chunkZ, int amount, boolean ignoreVeinStats, CallbackInfo ci) {
        if (!AOConfigHolder.INSTANCE.machines.bedrockDrillsDepleteResources)
            ci.cancel();
    }
}
