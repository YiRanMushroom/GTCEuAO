package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.yiranmushroom.gtceuao.gtceuao;
import it.unimi.dsi.fastutil.longs.LongIntMutablePair;
import it.unimi.dsi.fastutil.longs.LongIntPair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;

import javax.annotation.Nonnull;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(OverclockingLogic.class)
public abstract class OverclockingLogicMixin {
    @Final
    @Shadow(remap = false)
    public static double STANDARD_OVERCLOCK_DURATION_DIVISOR = ConfigHolder.INSTANCE.machines.overclockDivisor;
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

    /**
     * @author YiRanMushroom
     * @reason Want to make it quicker
     */
    @Nonnull
    @Overwrite(remap = false)
    public static @NotNull LongIntPair heatingCoilOverclockingLogic(long recipeEUt, long maximumVoltage, int recipeDuration, int maxOverclocks, int currentTemp, int recipeRequiredTemp) {
        int amountEUDiscount = Math.max(0, (currentTemp - recipeRequiredTemp) / 900);
        int amountPerfectOC = amountEUDiscount;
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
}
