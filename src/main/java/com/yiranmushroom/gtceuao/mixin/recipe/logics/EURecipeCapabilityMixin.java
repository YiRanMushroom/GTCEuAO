package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.content.SerializerLong;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.gtceuao;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static com.gregtechceu.gtceu.GTCEu.LOGGER;

@Mixin(EURecipeCapability.class)
public class EURecipeCapabilityMixin extends RecipeCapability<Long> {

    protected EURecipeCapabilityMixin() {
        super("eu", 0xFFFFFF00, false, 2, SerializerLong.INSTANCE);
    }

    /**
     * @author YiranMushroom
     * @reason Overwrite the limitParallel method to make it check if the voltage should be limited
     */
    @Overwrite(remap = false)
    public int limitParallel(GTRecipe recipe, IRecipeCapabilityHolder holder, int multiplier) {

        if (!AOConfigHolder.INSTANCE.machines.ParallelNeedMorePower) {
            return Integer.MAX_VALUE;
        }

        long maxVoltage = Long.MAX_VALUE;
        if (holder instanceof IOverclockMachine overclockMachine) {
            maxVoltage = overclockMachine.getOverclockVoltage();
        } else if (holder instanceof ITieredMachine tieredMachine) {
            maxVoltage = GTValues.V[tieredMachine.getTier()];
        }

        long recipeEUt = RecipeHelper.getInputEUt(recipe);
        if (recipeEUt == 0) {
            recipeEUt = RecipeHelper.getOutputEUt(recipe);
        }

        return Math.abs((int) (maxVoltage / recipeEUt));
    }

    /**
     * @author YiranMushroom
     * @reason try to fix parallel issues
     */
    @Overwrite(remap = false)
    public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
        return this.limitParallel(recipe, holder, parallelAmount);
    }
}
