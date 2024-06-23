package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.parts;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ParallelHatchPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParallelHatchPartMachine.class)
public class ParallelHatchPartMachineMixin {
/*    @Inject(method = "getCurrentParallel", remap = false, at = @At("RETURN"), cancellable = true)
    private void getCurrentParallelInj(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue((int) Math.min((long) cir.getReturnValue() *
            AOConfigHolder.INSTANCE.machines.ParallelMultiplier, Integer.MAX_VALUE));
    }*/

    @Mutable
    @Shadow(remap = false)
    @Final
    private int maxParallel;

    @Shadow(remap = false)
    private int currentParallel;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void initInj(IMachineBlockEntity holder, int tier, CallbackInfo ci) {
        this.maxParallel *= AOConfigHolder.INSTANCE.machines.ParallelMultiplier;
        if (this.currentParallel == 1) this.currentParallel = this.maxParallel;
    }
}
