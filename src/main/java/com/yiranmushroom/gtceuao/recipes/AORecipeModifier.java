package com.yiranmushroom.gtceuao.recipes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import lombok.val;

import javax.annotation.Nonnull;

import static com.gregtechceu.gtceu.common.block.CoilBlock.CoilType.*;
import static com.yiranmushroom.gtceuao.mixin.recipe.logics.OverclockingLogicMixin.PERFECT_OVERCLOCK;

public class AORecipeModifier {

    public static GTRecipe ebfOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            val blastFurnaceTemperature = coilMachine.getCoilType().getCoilTemperature() + 100 * Math.max(0, coilMachine.getTier() - GTValues.MV);
            if (!recipe.data.contains("ebf_temp") || recipe.data.getInt("ebf_temp") > blastFurnaceTemperature) {
                return null;
            }
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }

            var result = GTRecipeModifiers.accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getA() == recipe ? result.getA().copy() : result.getA();

            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> OverclockingLogic.heatingCoilOverclockingLogic(
                Math.abs(recipeEUt),
                maxVoltage,
                duration,
                amountOC,
                blastFurnaceTemperature,
                recipe1.data.contains("ebf_temp") ? recipe1.data.getInt("ebf_temp") : 0
            )), recipe, coilMachine.getMaxVoltage());

        }
        return null;
    }

    public static GTRecipe perfectCoilMachineParallel(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            val result = GTRecipeModifiers.accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getA() == recipe ? result.getA().copy() : result.getA();
            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, coilMachine.getMaxVoltage());
            return recipe;
        }
        return null;
    }

    public static GTRecipe perfectMachineParallel(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof WorkableElectricMultiblockMachine workableMachine) {
            val result = GTRecipeModifiers.accurateParallel(machine, recipe, AOConfigHolder.INSTANCE.machines.multiblockParallelAmount, false);
            recipe = result.getA() == recipe ? result.getA().copy() : result.getA();
            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, workableMachine.getMaxVoltage());
            return recipe;

        }
        return null;
    }

    public static GTRecipe pyrolyseOvenOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }
            val result = GTRecipeModifiers.accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getA() == recipe ? result.getA().copy() : result.getA();
            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                var pair = OverclockingLogic.NON_PERFECT_OVERCLOCK.getLogic().runOverclockingLogic(recipe1, recipeEUt, maxVoltage, duration, amountOC);
                if (coilMachine.getCoilTier() == 0) {
                    pair.second(pair.secondInt() * 5 / 4);
                } else {
                    pair.second(pair.secondInt() * 2 / (coilMachine.getCoilTier() + 1));
                }
                pair.second(Math.max(1, pair.secondInt()));
                return pair;
            }), recipe, coilMachine.getMaxVoltage());
        }
        return null;
    }

    public static GTRecipe crackerOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }

            val result = GTRecipeModifiers.accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
            recipe = result.getA() == recipe ? result.getA().copy() : result.getA();

            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                var pair = OverclockingLogic.NON_PERFECT_OVERCLOCK.getLogic().runOverclockingLogic(recipe1, recipeEUt, maxVoltage, duration, amountOC);
                if (coilMachine.getCoilTier() > 0) {
                    var eu = pair.firstLong() * (1 - coilMachine.getCoilTier() * 0.1);
                    pair.first((long) Math.max(1, eu));
                }
                return pair;
            }), recipe, coilMachine.getMaxVoltage());
        }
        return null;
    }

    public static int getParallelAmountByCoilType(ICoilType coilType) {
        if (coilType.equals(CUPRONICKEL)) {
            return 4;
        } else if (coilType.equals(KANTHAL)) {
            return 16;
        } else if (coilType.equals(NICHROME)) {
            return 64;
        } else if (coilType.equals(RTMALLOY)) {
            return 256;
        } else if (coilType.equals(HSSG)) {
            return 1024;
        } else if (coilType.equals(NAQUADAH)) {
            return 4096;
        } else if (coilType.equals(TRINIUM)) {
            return 16384;
        } else if (coilType.equals(TRITANIUM)) {
            return 65536;
        }
        return 1;
    }

}
