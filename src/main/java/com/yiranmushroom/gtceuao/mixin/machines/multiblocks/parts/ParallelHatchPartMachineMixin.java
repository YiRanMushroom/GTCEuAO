package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.parts;

import com.gregtechceu.gtceu.common.machine.multiblock.part.ParallelHatchPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParallelHatchPartMachine.class)
public class ParallelHatchPartMachineMixin {
    @Inject(method = "getCurrentParallel", remap = false, at = @At("RETURN"), cancellable = true)
    private void getCurrentParallelInj(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) Math.min((long) cir.getReturnValue() *
            AOConfigHolder.INSTANCE.machines.ParallelMultiplier, Integer.MAX_VALUE));
    }
}
