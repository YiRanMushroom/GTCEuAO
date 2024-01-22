package com.yiranmushroom.gtceuao.mixin.misc.bedrockDrills;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import dev.toma.configuration.config.Configurable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BedrockFluidVeinSavedData.class)
public class BedrockFluidVeinSavedDataMixin {
    @Inject(method = "depleteVein", at = @At("HEAD"), cancellable = true, remap = false)
    private void depleteVein(int chunkX, int chunkZ, int amount, boolean ignoreVeinStats, CallbackInfo ci){
        if (AOConfigHolder.INSTANCE.machines.bedrockDrillsDepleteResources)
            ci.cancel();
    }
}
