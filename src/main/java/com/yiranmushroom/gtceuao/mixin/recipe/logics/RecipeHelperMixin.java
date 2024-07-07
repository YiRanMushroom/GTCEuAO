package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import it.unimi.dsi.fastutil.longs.LongIntPair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeHelper.class)
public abstract class RecipeHelperMixin {
    @Shadow(remap = false)
    public static long getInputEUt(GTRecipe recipe) {
        return 0;
    }

    @Shadow(remap = false)
    public static LongIntPair performOverclocking(OverclockingLogic logic, @NotNull GTRecipe recipe, long EUt, long maxOverclockVoltage) {
        return null;
    }

    @Shadow(remap = false)
    public static long getOutputEUt(GTRecipe recipe) {
        return 0;
    }

    /**
     * @author YiRanMushroom
     * @reason Try to fix generator overclocking
     */
    @Overwrite(remap = false)
    public static GTRecipe applyOverclock(OverclockingLogic logic, @NotNull GTRecipe recipe, long maxOverclockVoltage) {
        long EUt = getInputEUt(recipe);
        if (EUt > 0) {
            var overclockResult = performOverclocking(logic, recipe, EUt, maxOverclockVoltage);
            if (overclockResult.leftLong() != EUt || recipe.duration != overclockResult.rightInt()) {
                recipe = recipe.copy();
                recipe.duration = overclockResult.rightInt();
                for (Content content : recipe.getTickInputContents(EURecipeCapability.CAP)) {
                    content.content = overclockResult.leftLong();
                }
            }
        }
        EUt = getOutputEUt(recipe);
        if (EUt > 0) {
            var overclockResult = performOverclocking(AOConfigHolder.INSTANCE.machines.boostGeneralGenerator ?
                new OverclockingLogic(1,4,1,1) :
                new OverclockingLogic(4,4,1,1),
                recipe, EUt, maxOverclockVoltage);
            if (overclockResult.leftLong() != EUt || recipe.duration != overclockResult.rightInt()) {
                recipe = recipe.copy();
                recipe.duration = overclockResult.rightInt();
                for (Content content : recipe.getTickOutputContents(EURecipeCapability.CAP)) {
                    content.content = overclockResult.leftLong();
                }
            }
        }
        return recipe;
    }
}
