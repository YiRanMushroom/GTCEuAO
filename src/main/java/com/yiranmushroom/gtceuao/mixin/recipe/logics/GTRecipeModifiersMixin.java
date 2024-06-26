package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.mojang.datafixers.util.Pair;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(GTRecipeModifiers.class)
public class GTRecipeModifiersMixin {
    @Unique
    private static Pair<GTRecipe, Integer> gtceuao$fastParallelNonGenerator(MetaMachine machine, GTRecipe recipe, int maxParallel, boolean modifyDuration) {
        // We want to use binary search to find the maximum amount.
        if (machine instanceof IRecipeCapabilityHolder holder) {
            int left = 1, right = maxParallel, mid;
            while (left < right) {
                mid = left + (right - left + 1) / 2;
                var copied = recipe.copy(ContentModifier.multiplier(mid), modifyDuration);
                if (copied.matchRecipe(holder).isSuccess() && copied.matchTickRecipe(holder).isSuccess()) {
                    left = mid;
                } else right = mid - 1;
            }
            return Pair.of(recipe.copy(ContentModifier.multiplier(left), modifyDuration), left);
        }
        return Pair.of(recipe, 1);
    }

    /**
     * @author YiranMushroom
     * @reason Use this to determine if the generator should use more fuel, false if ParallelNeedMorePower is false
     */
    @Overwrite(remap = false)
    public static Pair<GTRecipe, Integer> fastParallel(MetaMachine machine, @NotNull GTRecipe recipe, int maxParallel, boolean modifyDuration) {
        if (machine instanceof IRecipeCapabilityHolder holder) {
            while (maxParallel > 0) {
                var copied = recipe.copy(ContentModifier.multiplier(maxParallel), modifyDuration);
                if (!AOConfigHolder.INSTANCE.machines.ParallelNeedMorePower && copied.isFuel) {
                    copied = new GTRecipe(copied.recipeType, copied.id, recipe.inputs, copied.outputs,
                        recipe.tickInputs, copied.tickOutputs, copied.conditions, copied.ingredientActions, copied.data, copied.duration, copied.isFuel);
                }
                if (copied.matchRecipe(holder).isSuccess() && copied.matchTickRecipe(holder).isSuccess()) {
                    return Pair.of(copied, maxParallel);
                }
                maxParallel /= 2;
            }
        }
        return Pair.of(recipe, 1);
    }

    /**
     * @author YiranMushroom
     * @reason Overwrite this to accommodate the parallel logic when applying to multiblock machines
     */
    @Overwrite(remap = false)
    public static Pair<GTRecipe, Integer> accurateParallel(MetaMachine machine, @NotNull GTRecipe recipe, int maxParallel, boolean modifyDuration) {
        if (maxParallel == 1) {
            return Pair.of(recipe, 1);
        }
        if (AOConfigHolder.INSTANCE.machines.legacyParallelLogic) {
            return gtceuao$fastParallelNonGenerator(machine, recipe, maxParallel, modifyDuration);
        }
        if (machine instanceof WorkableElectricMultiblockMachine workableMachine)
            return ParallelLogic.applyParallel(machine, recipe,
                AOConfigHolder.INSTANCE.machines.ParallelNeedMorePower ? (int) Math.min(maxParallel, (workableMachine.getMaxVoltage() / RecipeHelper.getInputEUt(recipe))) : maxParallel,
                modifyDuration);
        else return ParallelLogic.applyParallel(machine, recipe,
            maxParallel,
            modifyDuration);
    }

    /**
     * @author YiranMushroom
     * @reason Trying to fix the parallel logic for hatch parallel
     */
    @Overwrite(remap = false)
    public static Pair<GTRecipe, Integer> hatchParallel(MetaMachine machine, @NotNull GTRecipe recipe,
                                                        boolean modifyDuration) {
        if (machine instanceof IMultiController controller && controller.isFormed()) {
            Optional<IParallelHatch> optional = controller.getParts().stream().filter(IParallelHatch.class::isInstance)
                .map(IParallelHatch.class::cast).findAny();
            if (optional.isPresent()) {
                IParallelHatch hatch = optional.get();
                return GTRecipeModifiers.accurateParallel(machine, recipe, hatch.getCurrentParallel(), modifyDuration);
            } else
                return GTRecipeModifiers.accurateParallel(machine, recipe, AOConfigHolder.INSTANCE
                    .machines.ParallelMultiplier, modifyDuration);
        }
        return Pair.of(recipe, 1);
    }
}