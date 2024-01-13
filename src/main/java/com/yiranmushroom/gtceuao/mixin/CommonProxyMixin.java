package com.yiranmushroom.gtceuao.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.gregtechceu.gtceu.common.CommonProxy")
public class CommonProxyMixin {
    @Inject(at = @At("HEAD"), method = "init", remap = false)
    private static void init(CallbackInfo ci) {
        com.yiranmushroom.gtceuao.config.MixinConfigHolder.init();
    }
}
