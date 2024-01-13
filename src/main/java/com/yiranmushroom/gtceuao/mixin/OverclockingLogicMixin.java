package com.yiranmushroom.gtceuao.mixin;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import it.unimi.dsi.fastutil.longs.LongIntPair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import com.yiranmushroom.gtceuao.config.MixinConfigHolder;

import javax.annotation.Nonnull;

import static com.gregtechceu.gtceu.api.recipe.OverclockingLogic.standardOverclockingLogic;

@Mixin(OverclockingLogic.class)
public abstract class OverclockingLogicMixin {
    @Final
    @Shadow
    public static double STANDARD_OVERCLOCK_DURATION_DIVISOR;
    @Final
    @Shadow
    public static final double STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER = MixinConfigHolder.INSTANCE.machines.overclockMultiplier;
//    public static final double STANDARD_OVERCLOCK_VOLTAGE_MULTIPLIER = 1.0;
    @Final
    @Shadow
    public static final double PERFECT_OVERCLOCK_DURATION_DIVISOR = Math.pow(STANDARD_OVERCLOCK_DURATION_DIVISOR, MixinConfigHolder.INSTANCE.machines.ExpPerfect);
//    public static final double PERFECT_OVERCLOCK_DURATION_DIVISOR = Math.pow(STANDARD_OVERCLOCK_DURATION_DIVISOR, 3.0);
    /**
     * @author YiRanMushroom
     * @reason Want to make it quicker
     */
    @Nonnull
    @Overwrite(remap = false)
    public static LongIntPair heatingCoilOverclockingLogic(long recipeEUt, long maximumVoltage, int recipeDuration, int maxOverclocks, int currentTemp, int recipeRequiredTemp) {
        int amountEUDiscount = Math.max(0, (currentTemp - recipeRequiredTemp) / 900);
        int amountPerfectOC = amountEUDiscount;
        recipeEUt = (long)((double)recipeEUt * Math.min(1.0, Math.pow(0.95, (double)amountEUDiscount)));
        if (amountPerfectOC > 0) {
            LongIntPair overclock = standardOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, amountPerfectOC, 4.0, 4.0);
            return standardOverclockingLogic(overclock.leftLong(), maximumVoltage, overclock.rightInt(), maxOverclocks - amountPerfectOC, STANDARD_OVERCLOCK_DURATION_DIVISOR, 4.0);
        } else {
            return standardOverclockingLogic(recipeEUt, maximumVoltage, recipeDuration, maxOverclocks, STANDARD_OVERCLOCK_DURATION_DIVISOR, 4.0);
        }
    }
}
