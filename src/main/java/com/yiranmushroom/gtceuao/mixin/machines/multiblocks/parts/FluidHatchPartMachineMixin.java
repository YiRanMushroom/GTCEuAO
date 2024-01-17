package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.parts;

import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidHatchPartMachine.class)
public class FluidHatchPartMachineMixin {
    @Final
    @Shadow(remap = false)
    public static final long INITIAL_TANK_CAPACITY_1X = (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches ? 4 : 8) * FluidHelper.getBucket();
    @Final
    @Shadow(remap = false)
    public static final long INITIAL_TANK_CAPACITY_4X = (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches ? 4 : 2) * FluidHelper.getBucket();
    @Final
    @Shadow(remap = false)
    public static final long INITIAL_TANK_CAPACITY_9X = (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches ? 4 : 1) * FluidHelper.getBucket();

    @Inject(method = "getTankCapacity", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getTankCapacity(long initialCapacity, int tier, CallbackInfoReturnable<Long> cir) {
        if (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches)
            cir.setReturnValue(initialCapacity * (1L << (2 * Math.min(9, tier))));
    }
}
