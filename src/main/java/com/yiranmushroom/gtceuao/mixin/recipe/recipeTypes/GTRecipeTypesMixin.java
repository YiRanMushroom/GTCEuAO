package com.yiranmushroom.gtceuao.mixin.recipe.recipeTypes;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.yiranmushroom.gtceuao.recipes.AORecipeTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GTRecipeTypes.class)
public class GTRecipeTypesMixin {

    @Final
    @Mutable
    @Shadow(remap = false)
    public static GTRecipeType ASSEMBLY_LINE_RECIPES;

    @Inject(method = "<clinit>", at = @At("RETURN"), remap = false)
    private static void clinitInj(CallbackInfo ci) {
        AORecipeTypes.registerRecipeTypes();
    }
}
