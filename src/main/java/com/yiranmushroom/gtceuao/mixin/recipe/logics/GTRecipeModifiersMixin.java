package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.util.Tuple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(GTRecipeModifiers.class)
public class GTRecipeModifiersMixin {
    /**
     * @author YiranMushroom
     * @reason Need to modify this to change energy consumption
     */
    @Overwrite(remap = false)
    private static Tuple<GTRecipe, Integer> tryParallel(IRecipeCapabilityHolder holder, GTRecipe original, int min, int max, boolean modifyDuration) {
        if (min > max) {
            return null;
        } else {
            int mid = (min + max) / 2;
            var oriEUt = original.tickInputs;
            GTRecipe copied = original.copy(ContentModifier.multiplier((double) mid), modifyDuration);
            if (!AOConfigHolder.INSTANCE.machines.ParallelNeedMorePower) {
                copied = new GTRecipe(copied.recipeType, copied.id, copied.inputs, copied.outputs,
                        oriEUt, copied.tickOutputs, copied.conditions, copied.data, copied.duration, copied.isFuel);
                LOGGER.info("Original Recipe has a EUt of " + EURecipeCapability.CAP.of(oriEUt.get(EURecipeCapability.CAP).get(0).content) + ", copied recipe has a EUt of " + EURecipeCapability.CAP.of(copied.tickInputs.get(EURecipeCapability.CAP).get(0).content) + ".");
            }


            if (copied.matchRecipe(holder).isSuccess() && copied.matchTickRecipe(holder).isSuccess()) {
                if (mid == max) {
                    return new Tuple<>(copied, mid);
                } else {
                    Tuple<GTRecipe, Integer> tryMore = tryParallel(holder, original, mid + 1, max, modifyDuration);
                    return tryMore != null ? tryMore : new Tuple<>(copied, mid);
                }
            } else {
                return tryParallel(holder, original, min, mid - 1, modifyDuration);
            }
        }
    }
}
