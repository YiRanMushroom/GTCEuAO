package com.yiranmushroom.gtceuao.mixin.registries;


import com.lowdragmc.lowdraglib.CommonProxy;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommonProxy.class)
public class CommonProxyMixin {
    @Inject(at = @At("HEAD"), method = "init", remap = false)
    private static void init(CallbackInfo ci) {
        AOConfigHolder.init();
    }
}
