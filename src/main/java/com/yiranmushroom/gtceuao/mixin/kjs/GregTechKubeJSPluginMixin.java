package com.yiranmushroom.gtceuao.mixin.kjs;

import com.gregtechceu.gtceu.integration.kjs.GregTechKubeJSPlugin;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifiers;
import com.yiranmushroom.gtceuao.recipes.AORecipeTypes;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GregTechKubeJSPlugin.class)
public class GregTechKubeJSPluginMixin {
    @Inject(method = "registerBindings", at = @At(value = "CONSTANT", args = "stringValue=GTRecipeModifiers", shift = At.Shift.AFTER), remap = false)
    private void registerBindingsInj(BindingsEvent event, CallbackInfo ci) {
        event.add("AORecipeModifiers", AORecipeModifiers.class);
        event.add("AORecipeTypes", AORecipeTypes.class);
    }
}
