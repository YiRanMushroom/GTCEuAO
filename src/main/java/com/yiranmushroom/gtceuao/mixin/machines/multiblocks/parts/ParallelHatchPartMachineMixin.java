package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.parts;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ParallelHatchPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParallelHatchPartMachine.class)
public class ParallelHatchPartMachineMixin extends TieredPartMachine {

    @Mutable
    @Shadow(remap = false)
    @Final
    private int maxParallel;

    @Shadow(remap = false)
    private int currentParallel;

    @Unique
    private boolean gtceuao$initialized = false;

    public ParallelHatchPartMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void initInj(IMachineBlockEntity holder, int tier, CallbackInfo ci) {
        this.maxParallel *= AOConfigHolder.INSTANCE.machines.ParallelMultiplier;
    }

    @Inject(method = "getCurrentParallel", at = @At("HEAD"), remap = false, cancellable = true)
    private void getCurrentParallelInj(CallbackInfoReturnable<Integer> cir) {
        if (currentParallel == 0) cir.setReturnValue(maxParallel);
    }

    @Inject(method = "setCurrentParallel", at = @At("HEAD"), remap = false)
    private void setCurrentParallelInj(int parallelAmount, CallbackInfo ci) {
        if (currentParallel == 0) currentParallel = maxParallel;
    }
}
