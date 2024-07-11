package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.utils.GTUtil;
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

    /**
     * @author YiranMushroom
     * @reason Try to fix overclock bonus
     */
    @Overwrite(remap = false)
    public static LongIntPair performOverclocking(OverclockingLogic logic, @NotNull GTRecipe recipe, long EUt,
                                                  long maxOverclockVoltage) {
        int recipeTier = GTUtil.getTierByVoltage(EUt);
        int maximumTier = maxOverclockVoltage < Integer.MAX_VALUE ? GTUtil.getTierByVoltage(maxOverclockVoltage) :
            GTUtil.getFakeVoltageTier(maxOverclockVoltage);
        // The maximum number of overclocks is determined by the difference between the tier the recipe is running at,
        // and the maximum tier that the machine can overclock to.
        int numberOfOCs = maximumTier - recipeTier + AOConfigHolder.INSTANCE.machines.bonusOfOCs;
        if (numberOfOCs <= 0) return LongIntPair.of(EUt, recipe.duration);
        if (recipeTier == GTValues.ULV) numberOfOCs--; // no ULV overclocking

        // Always overclock even if numberOfOCs is <=0 as without it, some logic for coil bonuses ETC won't apply.
        return logic.getLogic().runOverclockingLogic(recipe, EUt, maxOverclockVoltage, recipe.duration, numberOfOCs);
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
                    new OverclockingLogic(1,4) :
                    new OverclockingLogic(4,4),
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
