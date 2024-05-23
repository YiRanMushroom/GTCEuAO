package com.yiranmushroom.gtceuao.mixin.recipe.condition;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.recipe.RockBreakerCondition;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RockBreakerCondition.class)
public class RockBreakerConditionMixin {
    @Inject(method = "test", at = @At("HEAD"), cancellable = true, remap = false)
    private void test(GTRecipe recipe, RecipeLogic recipeLogic, CallbackInfoReturnable<Boolean> cir) {
        if (AOConfigHolder.INSTANCE.machines.rockBreakerIgnoreConditions) {
            cir.setReturnValue(true);
        }
    }
}
