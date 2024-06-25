package com.yiranmushroom.gtceuao.recipes;

import com.google.errorprone.annotations.Var;
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
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.mojang.datafixers.util.Pair;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.longs.LongIntPair;
import kroppeb.stareval.function.Type;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Objects;

import static com.gregtechceu.gtceu.api.recipe.OverclockingLogic.STANDARD_OVERCLOCK_DURATION_DIVISOR;
import static com.gregtechceu.gtceu.common.block.CoilBlock.CoilType.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.accurateParallel;
import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

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
//            var result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
//            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();
//
//            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> OverclockingLogic.heatingCoilOverclockingLogic(
//                Math.abs(recipeEUt),
//                maxVoltage,
//                duration,
//                amountOC,
//                blastFurnaceTemperature,
//                recipe1.data.contains("ebf_temp") ? recipe1.data.getInt("ebf_temp") : 0
//            )), recipe, coilMachine.getOverclockVoltage());
            final Pair<GTRecipe, Integer>[] result = new Pair[] { null };
            RecipeHelper.applyOverclock(
                new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                    var parallel = heatingCoilParallelOverclockingLogic(
                        Math.abs(recipeEUt),
                        maxVoltage,
                        duration,
                        amountOC,
                        blastFurnaceTemperature,
                        recipe1.data.contains("ebf_temp") ? recipe1.data.getInt("ebf_temp") : 0);

                    result[0] = GTRecipeModifiers.accurateParallel(machine, recipe, (long)parallel.getRight()
                            *AOConfigHolder.INSTANCE.machines.ParallelMultiplier > Integer.MAX_VALUE? Integer.MAX_VALUE:
                            parallel.getRight() * AOConfigHolder.INSTANCE.machines.ParallelMultiplier,
                        false);
                    return LongIntPair.of(parallel.getLeft(), parallel.getMiddle());
                }), recipe, coilMachine.getOverclockVoltage());
            return result[0].getFirst();
        }
        return null;
    }

    public static @NotNull ImmutableTriple<Long, Integer, Integer> heatingCoilParallelOverclockingLogic(long recipeEUt, long maximumVoltage, int recipeDuration, int maxOverclocks, int currentTemp, int recipeRequiredTemp) {
        int amountEUDiscount = Math.max(0, (currentTemp - recipeRequiredTemp) / 900);
        int amountPerfectOC = amountEUDiscount / 2;
        recipeEUt = (long)((double)recipeEUt * Math.min(1.0, Math.pow(0.95, (double)amountEUDiscount)));
        if (amountPerfectOC > 0) {
            var overclock = OverclockingLogic.standardOverclockingLogicWithSubTickParallelCount(recipeEUt, maximumVoltage, recipeDuration, amountPerfectOC
                , (int)Math.pow(STANDARD_OVERCLOCK_DURATION_DIVISOR, AOConfigHolder.INSTANCE.machines.ExpPerfect),
                AOConfigHolder.INSTANCE.machines.overclockMultiplier);
            if (overclock.getRight() != 1) return new ImmutableTriple<>(overclock.getLeft(), overclock.getMiddle(),
                (long)overclock.getMiddle() * (long)Math.pow(ConfigHolder.INSTANCE.machines.overclockDivisor,
                    AOConfigHolder.INSTANCE.machines.ExpPerfect) > Integer.MAX_VALUE? Integer.MAX_VALUE:
                    overclock.getMiddle() * (int)Math.pow(ConfigHolder.INSTANCE.machines.overclockDivisor,
                        AOConfigHolder.INSTANCE.machines.ExpPerfect)
                );
            else return OverclockingLogic.standardOverclockingLogicWithSubTickParallelCount(overclock.getLeft(), maximumVoltage, overclock.getMiddle(), maxOverclocks - amountPerfectOC, STANDARD_OVERCLOCK_DURATION_DIVISOR,
                AOConfigHolder.INSTANCE.machines.overclockMultiplier);
        } else {
            return OverclockingLogic.standardOverclockingLogicWithSubTickParallelCount(recipeEUt, maximumVoltage,  recipeDuration, maxOverclocks, STANDARD_OVERCLOCK_DURATION_DIVISOR,
                AOConfigHolder.INSTANCE.machines.overclockMultiplier);
        }
    }

    public static GTRecipe perfectCoilMachineParallel(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
//            val result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
//            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();
//            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, coilMachine.getOverclockVoltage());
//            return recipe;
            return prefectSubtickParallel(coilMachine,recipe,false, (it) ->
                getParallelAmountByCoilType((ICoilType) it));
        } else {
            return null;
        }
    }

//    public static GTRecipe perfectMachineParallel(MetaMachine machine, @Nonnull GTRecipe recipe) {
//        if (machine instanceof WorkableElectricMultiblockMachine workableMachine) {
//
//            // apply parallel first
//            var parallel = Objects.requireNonNull(GTRecipeModifiers.accurateParallel(
//                machine, recipe, AOConfigHolder.INSTANCE.machines.multiblockParallelAmount, false));
//
//            recipe = parallel.getFirst();
//
//            // apply overclock afterward
//            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, workableMachine.getOverclockVoltage());
//
//            return recipe;
//        }
//        return null;
//    }

    public static GTRecipe pyrolyseOvenOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }
//            val result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
//            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();
//            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
//                var pair = OverclockingLogic.NON_PERFECT_OVERCLOCK.getLogic().runOverclockingLogic(recipe1, recipeEUt, maxVoltage, duration, amountOC);
//                if (coilMachine.getCoilTier() == 0) {
//                    pair.second(pair.secondInt() * 5 / 4);
//                } else {
//                    pair.second(pair.secondInt() * 2 / (coilMachine.getCoilTier() + 1));
//                }
//                pair.second(Math.max(1, pair.secondInt()));
//                return pair;
//            }), recipe, coilMachine.getOverclockVoltage());
            final Pair<GTRecipe, Integer>[] result = new Pair[] { null };
            RecipeHelper.applyOverclock(
                new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                    var parallel = OverclockingLogic.standardOverclockingLogicWithSubTickParallelCount(
                        Math.abs(recipeEUt),
                        maxVoltage,
                        duration,
                        amountOC,
                        (int)Math.pow( STANDARD_OVERCLOCK_DURATION_DIVISOR, AOConfigHolder.INSTANCE
                            .machines.ExpPerfect),
                        OverclockingLogic.STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);

                    result[0] = GTRecipeModifiers.accurateParallel(machine, recipe, (long)parallel.getRight()
                            *AOConfigHolder.INSTANCE.machines.ParallelMultiplier > Integer.MAX_VALUE? Integer.MAX_VALUE:
                            parallel.getRight() * AOConfigHolder.INSTANCE.machines.ParallelMultiplier,
                        false);

                    if (coilMachine.getCoilTier() == 0) {
//                        var eu = parallel.getLeft() * (1 - coilMachine.getCoilTier() * 0.1);
                        return LongIntPair.of(parallel.getLeft(), parallel.getMiddle() * 5 / 4);
                    }

                    return LongIntPair.of(parallel.getLeft(), parallel.getMiddle()* 2 / (coilMachine.getCoilTier() + 1));

                }), recipe, coilMachine.getOverclockVoltage());
            return result[0].getFirst();
        }
        return null;
    }

    public static GTRecipe crackerOverclock(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            if (RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()) {
                return null;
            }

//            val result = accurateParallel(machine, recipe, getParallelAmountByCoilType(coilMachine.getCoilType()), false);
//            recipe = result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst();
//
//            return RecipeHelper.applyOverclock(new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
//                var pair = OverclockingLogic.NON_PERFECT_OVERCLOCK.getLogic().runOverclockingLogic(recipe1, recipeEUt, maxVoltage, duration, amountOC);
//                if (coilMachine.getCoilTier() > 0) {
//                    var eu = pair.firstLong() * (1 - coilMachine.getCoilTier() * 0.1);
//                    pair.first((long) Math.max(1, eu));
//                }
//                return pair;
//            }), recipe, coilMachine.getOverclockVoltage());

            final Pair<GTRecipe, Integer>[] result = new Pair[] { null };
            RecipeHelper.applyOverclock(
                new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                    var parallel = OverclockingLogic.standardOverclockingLogicWithSubTickParallelCount(
                        Math.abs(recipeEUt),
                        maxVoltage,
                        duration,
                        amountOC,
                        (int)Math.pow( STANDARD_OVERCLOCK_DURATION_DIVISOR, AOConfigHolder.INSTANCE
                            .machines.ExpPerfect),
                        OverclockingLogic.STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);

                    result[0] = GTRecipeModifiers.accurateParallel(machine, recipe, (long)parallel.getRight()
                            *AOConfigHolder.INSTANCE.machines.ParallelMultiplier > Integer.MAX_VALUE? Integer.MAX_VALUE:
                            parallel.getRight() * AOConfigHolder.INSTANCE.machines.ParallelMultiplier,
                        false);

                    if (coilMachine.getCoilTier() > 0) {
                    var eu = parallel.getLeft() * (1 - coilMachine.getCoilTier() * 0.1);
                        return LongIntPair.of(Math.max(1,(int)eu), parallel.getMiddle());
                    }

                    return LongIntPair.of(parallel.getLeft(), parallel.getMiddle());

                }), recipe, coilMachine.getOverclockVoltage());
            return result[0].getFirst();

        }
        return null;
    }

//    private static final HashMap<ICoilType, Integer> coilParallelMapper = new HashMap<>();

    public static int getParallelAmountByCoilType(ICoilType coilType) {
        return AOConfigHolder.INSTANCE.machines.ParallelMultiplier *
            coilType.getLevel() * coilType.getEnergyDiscount();
    }

    public static GTRecipe prefectSubtickParallel(MetaMachine machine, @NotNull GTRecipe recipe, boolean modifyDuration) {
        if (machine instanceof WorkableElectricMultiblockMachine electricMachine) {
            final Pair<GTRecipe, Integer>[] result = new Pair[] { null };
            RecipeHelper.applyOverclock(
                new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                    var parallel = OverclockingLogic.standardOverclockingLogicWithSubTickParallelCount(
                        Math.abs(recipeEUt),
                        maxVoltage,
                        duration,
                        amountOC,
                        (int)Math.pow( STANDARD_OVERCLOCK_DURATION_DIVISOR, AOConfigHolder.INSTANCE
                            .machines.ExpPerfect),
                        OverclockingLogic.STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);

                    result[0] = GTRecipeModifiers.accurateParallel(machine, recipe, (long)parallel.getRight()
                        *AOConfigHolder.INSTANCE.machines.ParallelMultiplier > Integer.MAX_VALUE? Integer.MAX_VALUE:
                        parallel.getRight() * AOConfigHolder.INSTANCE.machines.ParallelMultiplier,
                        modifyDuration);
                    return LongIntPair.of(parallel.getLeft(), parallel.getMiddle());
                }), recipe, electricMachine.getOverclockVoltage());
            return result[0].getFirst();
        }
        return null;
    }

    public static <T extends WorkableElectricMultiblockMachine> GTRecipe prefectSubtickParallel(T machine, @NotNull GTRecipe recipe,
                                                  boolean modifyDuration,
                                                  Function<T, Integer> function) {

        final Pair<GTRecipe, Integer>[] result = new Pair[] { null };
        RecipeHelper.applyOverclock(
            new OverclockingLogic((recipe1, recipeEUt, maxVoltage, duration, amountOC) -> {
                var parallel = OverclockingLogic.
                    standardOverclockingLogicWithSubTickParallelCount(
                    Math.abs(recipeEUt),
                    maxVoltage,
                    duration,
                    amountOC,
                    (int)Math.pow( STANDARD_OVERCLOCK_DURATION_DIVISOR, AOConfigHolder.INSTANCE
                        .machines.ExpPerfect),
                    OverclockingLogic.STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);

                var bonus = function.apply(machine);

                result[0] = GTRecipeModifiers.accurateParallel(machine, recipe, (long)parallel.getRight()
                        *bonus > Integer.MAX_VALUE? Integer.MAX_VALUE:
                        parallel.getRight() * bonus,
                    modifyDuration);
                return LongIntPair.of(parallel.getLeft(), parallel.getMiddle());
            }), recipe, machine.getOverclockVoltage());
        return result[0].getFirst();
    }

    public static final RecipeModifier PERFECT_SUBTICK_PARALLEL = (machine, recipe) -> AORecipeModifier
        .prefectSubtickParallel(machine, recipe, false);
}
