package com.yiranmushroom.gtceuao.recipes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.mojang.datafixers.util.Pair;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import it.unimi.dsi.fastutil.longs.LongIntPair;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.accurateParallel;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.fastParallel;

public class AORecipeModifiers {

    public static GTRecipe ebfOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            val blastFurnaceTemperature = coilMachine.getCoilType().getCoilTemperature() + 100 * Math.max(0, coilMachine.getTier() - GTValues.MV);
            if (!recipe.data.contains("ebf_temp") || recipe.data.getInt("ebf_temp") > blastFurnaceTemperature) {
                return null;
            }
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }

            var result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();

            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> OverclockingLogic.heatingCoilOverclockingLogic(
                Math.abs(recipeEUt),
                maxVoltage,
                duration,
                amountOC,
                blastFurnaceTemperature,
                recipe1.data.contains("ebf_temp") ? recipe1.data.getInt("ebf_temp") : 0
            )), recipe, coilMachine.getOverclockVoltage());

        }
        return null;
    }

    public static GTRecipe perfectCoilMachineParallel(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            val result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();
            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, coilMachine.getOverclockVoltage());
            return recipe;
        } else {
            return perfectMachineParallel(machine, recipe);
        }
    }

    public static GTRecipe perfectMachineParallel(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof WorkableElectricMultiblockMachine workableMachine) {

            // apply parallel first
            var parallel = Objects.requireNonNull(GTRecipeModifiers.accurateParallel(
                machine, recipe, AOConfigHolder.INSTANCE.machines.multiblockParallelAmount, false));

            recipe = parallel.getFirst();

            // apply overclock afterward
            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, workableMachine.getOverclockVoltage());

            return recipe;
        }
        return null;
    }

    public static GTRecipe pyrolyseOvenOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }
            val result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();
            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                var pair = OverclockingLogic.NON_PERFECT_OVERCLOCK.getLogic().runOverclockingLogic(recipe1, recipeEUt, maxVoltage, duration, amountOC);
                if (coilMachine.getCoilTier() == 0) {
                    pair.second(pair.secondInt() * 5 / 4);
                } else {
                    pair.second(pair.secondInt() * 2 / (coilMachine.getCoilTier() + 1));
                }
                pair.second(Math.max(1, pair.secondInt()));
                return pair;
            }), recipe, coilMachine.getOverclockVoltage());
        }
        return null;
    }

    public static GTRecipe crackerOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }

            val result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();

            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                var pair = OverclockingLogic.NON_PERFECT_OVERCLOCK.getLogic().runOverclockingLogic(recipe1, recipeEUt, maxVoltage, duration, amountOC);
                if (coilMachine.getCoilTier() > 0) {
                    var eu = pair.firstLong() * (1 - coilMachine.getCoilTier() * 0.1);
                    pair.first((long) Math.max(1, eu));
                }
                return pair;
            }), recipe, coilMachine.getOverclockVoltage());
        }
        return null;
    }

//    private static final HashMap<ICoilType, Integer> coilParallelMapper = new HashMap<>();

    public static int getParallelAmountByCoilType(ICoilType coilType) {
        return AOConfigHolder.INSTANCE.machines.ParallelMultiplier *
            coilType.getLevel() * coilType.getEnergyDiscount();
    }

    public static final RecipeModifier PERFECT_SUBTICK_PARALLEL = (machine, recipe) ->
        prefectSubtickParallel(machine, recipe, false);


    public static GTRecipe prefectSubtickParallel(MetaMachine machine, @NotNull GTRecipe recipe, boolean modifyDuration) {
        if (machine instanceof WorkableElectricMultiblockMachine electricMachine) {
            Pair<GTRecipe, Integer>[] result = new Pair[]{null};
            RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                ImmutableTriple<Long, Integer, Integer> parallel = OverclockingLogic.standardOverclockingLogicWithSubTickParallelCount(Math.abs(recipeEUt), maxVoltage, duration, amountOC, OverclockingLogic.PERFECT_OVERCLOCK_DURATION_DIVISOR, AOConfigHolder.INSTANCE.machines.overclockMultiplier);
                result[0] = fastParallel(machine, recipe, parallel.getRight(), modifyDuration);
                return LongIntPair.of(parallel.getLeft(), parallel.getMiddle());
            }), recipe, electricMachine.getOverclockVoltage());
            if (result[0] != null) {
                return (GTRecipe)result[0].getFirst();
            }
        }

        return null;
    }
}
