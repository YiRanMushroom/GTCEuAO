package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.yiranmushroom.gtceuao.gtceuao;
import it.unimi.dsi.fastutil.longs.LongIntMutablePair;
import it.unimi.dsi.fastutil.longs.LongIntPair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

import java.util.logging.Logger;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(OverclockingLogic.class)
public abstract class OverclockingLogicMixin {
    @Final
    @Shadow(remap = false)
    @Mutable
    public static double STANDARD_OVERCLOCK_DURATION_DIVISOR;
    @Final
    @Shadow(remap = false)
    public static final double STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER = AOConfigHolder.INSTANCE.machines.overclockMultiplier;
    @Final
    @Shadow(remap = false)
    public static final double PERFECT_OVERCLOCK_DURATION_DIVISOR = Math.pow(STANDARD_OVERCLOCK_DURATION_DIVISOR, AOConfigHolder.INSTANCE.machines.ExpPerfect);

    @Final
    @Shadow(remap = false)
    public static final OverclockingLogic PERFECT_OVERCLOCK = new OverclockingLogic(PERFECT_OVERCLOCK_DURATION_DIVISOR, STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);
    @Final
    @Shadow(remap = false)
    public static final OverclockingLogic NON_PERFECT_OVERCLOCK = new OverclockingLogic(STANDARD_OVERCLOCK_DURATION_DIVISOR, STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);

    @Shadow(remap = false)
    protected OverclockingLogic.Logic logic;

    @Inject(method = "<clinit>", at = @At("HEAD"), remap = false)
    private static void clinitInj(CallbackInfo ci) {
        STANDARD_OVERCLOCK_DURATION_DIVISOR = AOConfigHolder.INSTANCE.machines.overclockDivisor;
    }

    @Inject(method = "<init>(DD)V", at = @At("RETURN"), remap = false)
    private void initInj(double durationDivisor, double voltageMultiplier, CallbackInfo ci) {
        this.logic = (recipe, recipeEUt, maxVoltage, duration, amountOC) -> standardOverclockingLogic(
            recipeEUt,
            maxVoltage,
            duration,
            amountOC,
            durationDivisor,
            voltageMultiplier);
    }

    /**
     * @author YiRanMushroom
     * @reason Want to make it quicker
     */
    @Nonnull
    @Overwrite(remap = false)
    public static @NotNull LongIntPair heatingCoilOverclockingLogic(long recipeEUt, long maximumVoltage, int recipeDuration, int maxOverclocks, int currentTemp, int recipeRequiredTemp) {
        int amountEUDiscount = Math.max(0, (currentTemp - recipeRequiredTemp) / 900);
        int amountPerfectOC = amountEUDiscount / 2;
        recipeEUt = (long) ((double) recipeEUt * Math.min(1.0, Math.pow(0.95, (double) amountEUDiscount)));
        if (amountPerfectOC > 0) {
            LongIntPair overclock = standardOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, amountPerfectOC, PERFECT_OVERCLOCK_DURATION_DIVISOR, STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);
            return standardOverclockingLogic(overclock.leftLong(), maximumVoltage, overclock.rightInt(), maxOverclocks - amountPerfectOC, STANDARD_OVERCLOCK_DURATION_DIVISOR, STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);
        } else {
            return standardOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, maxOverclocks, STANDARD_OVERCLOCK_DURATION_DIVISOR, STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER);
        }
    }


    /**
     * applies standard logic for overclocking, where each overclock modifies energy and duration
     *
     * @param recipeEUt         the EU/t of the recipe to overclock
     * @param maxVoltage        the maximum voltage the recipe is allowed to be run at
     * @param recipeDuration    the duration of the recipe to overclock
     * @param durationDivisor   the value to divide the duration by for each overclock
     * @param voltageMultiplier the value to multiply the voltage by for each overclock
     * @param numberOfOCs       the maximum amount of overclocks allowed
     * @return an int array of {OverclockedEUt, OverclockedDuration}
     * @author YiRanMushroom
     * @reason Need to fix the duration issue and add user allowed overclocking bonus time
     */
    @Overwrite(remap = false)
    @Nonnull
    public static @NotNull LongIntPair standardOverclockingLogic(long recipeEUt, long maxVoltage, int recipeDuration, int numberOfOCs, double durationDivisor, double voltageMultiplier) {
        if (recipeEUt < 0)
            return gtceuao$standardGeneratorOverclockingLogic(-recipeEUt, maxVoltage, recipeDuration, numberOfOCs, durationDivisor, voltageMultiplier);
        double resultDuration = recipeDuration;
        double resultVoltage = recipeEUt;

        for (; numberOfOCs > (-AOConfigHolder.INSTANCE.machines.bonusOfOCs); numberOfOCs--) {
            // it is important to do voltage first,
            // so overclocking voltage does not go above the limit before changing duration

            double potentialVoltage = resultVoltage * voltageMultiplier;
            // do not allow voltage to go above maximum
            if (potentialVoltage > maxVoltage) break;

            double potentialDuration = resultDuration / durationDivisor;
            // do not allow duration to go below one tick
            if (potentialDuration < 1) {
                potentialDuration = 1;
                resultDuration = potentialDuration;
                break;
            }
            // update the voltage for the next iteration after everything else
            resultDuration = potentialDuration;
            // in case duration overclocking would waste energy
            resultVoltage = potentialVoltage;
        }

        return LongIntMutablePair.of((long) resultVoltage, (int) resultDuration);
    }

    @SuppressWarnings("all")
    @Overwrite(remap = false)
    @NotNull
    public static ImmutableTriple<Long, Integer, Integer>
    standardOverclockingLogicWithSubTickParallelCount(long recipeEUt,
                                                      long maxVoltage,
                                                      int recipeDuration,
                                                      int numberOfOCs,
                                                      double durationDivisor,
                                                      double voltageMultiplier) {
        double resultDuration = recipeDuration;
        double resultVoltage = recipeEUt;
        double resultParallel = 1;

        for (; numberOfOCs > (-AOConfigHolder.INSTANCE.machines.bonusOfOCs); numberOfOCs--) {
            // it is important to do voltage first,
            // so overclocking voltage does not go above the limit before changing duration

            double potentialVoltage = resultVoltage * voltageMultiplier;
            // do not allow voltage to go above maximum
            if (potentialVoltage > maxVoltage) break;

            double potentialDuration = resultDuration / durationDivisor;
            // do not allow duration to go below one tick
            if (potentialDuration < 1) resultParallel /= potentialDuration;
            // update the duration for the next iteration
            resultDuration = resultParallel > 1 ? 1 : potentialDuration;

            // update the voltage for the next iteration after everything else
            // in case duration overclocking would waste energy
            resultVoltage = potentialVoltage;

            if (resultParallel > AOConfigHolder.INSTANCE.machines.subtickParallelLimit) {
                resultParallel = AOConfigHolder.INSTANCE.machines.subtickParallelLimit;
                break;
            }
        }

        return ImmutableTriple.of((long) resultVoltage, (int) resultDuration, (int) resultParallel);
    }

    @Unique
    private static @NotNull LongIntPair gtceuao$standardGeneratorOverclockingLogic(Long recipeEUt, Long maxVoltage, int recipeDuration, int maxOverclocks, double durationDivisor, double voltageMultiplier) {
        double resultDuration = recipeDuration;
        double resultVoltage = recipeEUt;

        for (; maxOverclocks > 0; maxOverclocks--) {
            // it is important to do voltage first,
            // so overclocking voltage does not go above the limit before changing duration

            double potentialVoltage = resultVoltage * 4;
            // do not allow voltage to go above maximum
            if (potentialVoltage > maxVoltage) {
                resultVoltage = maxVoltage;
                resultDuration = resultDuration / voltageMultiplier * voltageMultiplier;
                break;
            }

            double potentialDuration = resultDuration / voltageMultiplier * voltageMultiplier;
            // do not allow duration to go below one tick
            if (potentialDuration < 1) {
                potentialDuration = 1;
                resultDuration = potentialDuration;
                break;
            }
            // update the voltage for the next iteration after everything else
            resultDuration = potentialDuration;
            // in case duration overclocking would waste energy
            resultVoltage = potentialVoltage;
        }

        return LongIntMutablePair.of((long) resultVoltage, (int) resultDuration);
    }
}
