package com.yiranmushroom.gtceuao.mixin.recipe.condition;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.recipe.CleanroomCondition;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CleanroomCondition.class)
public class CleanroomConditionMixin {
    @Inject(method = "test", at = @At("HEAD"), cancellable = true, remap = false)
    private void test(GTRecipe recipe, RecipeLogic recipeLogic, CallbackInfoReturnable<Boolean> cir) {
        if (AOConfigHolder.INSTANCE.machines.disableCleanroomCheck) {
            cir.setReturnValue(true);
        }
    }
}
