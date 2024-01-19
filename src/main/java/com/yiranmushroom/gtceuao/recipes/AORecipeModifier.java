package com.yiranmushroom.gtceuao.recipes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import lombok.val;

import javax.annotation.Nonnull;

import static com.gregtechceu.gtceu.common.block.CoilBlock.CoilType.*;

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

            GTRecipe finalRecipe = recipe;
            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> OverclockingLogic.heatingCoilOverclockingLogic(
                    Math.abs(recipeEUt),
                    maxVoltage,
                    duration,
                    amountOC,
                    blastFurnaceTemperature,
                    finalRecipe.data.contains("ebf_temp") ? finalRecipe.data.getInt("ebf_temp") : 0
            )), recipe, coilMachine.getMaxVoltage());

//            var result = GTRecipeModifiers.accurateParallel(machine, newRecipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
//            recipe = result.getA() == recipe ? result.getA().copy() : result.getA();
//            return result.getA();
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
        } else if (coilType.equals(HSSG)) {
            return 256;
        } else if (coilType.equals(NAQUADAH)) {
            return 1024;
        } else if (coilType.equals(TRINIUM)) {
            return 4096;
        } else if (coilType.equals(TRITANIUM)) {
            return 16384;
        }
        return 1;
    }

}
