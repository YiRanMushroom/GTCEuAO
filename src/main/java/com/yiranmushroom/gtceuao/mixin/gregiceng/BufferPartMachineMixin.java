package com.yiranmushroom.gtceuao.mixin.gregiceng;

import com.epimorphismmc.gregiceng.common.machine.multiblock.part.BufferPartMachine;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BufferPartMachine.class, remap = false)
public abstract class BufferPartMachineMixin extends TieredIOPartMachine implements IDistinctPart, IMachineModifyDrops {
    public BufferPartMachineMixin(IMachineBlockEntity holder, int tier, IO io) {
        super(holder, tier, io);
    }

    @Inject(method = "getInventorySize", require = 0, at = @At("HEAD"), remap = false, cancellable = true)
    private void getInventorySizeInj(CallbackInfoReturnable<Integer> cir) {
        if (!AOConfigHolder.INSTANCE.machines.buffBusesAndHatches)
            return;
        int sizeRoot = Math.min(10, 1 + 2 * this.getTier());
        cir.setReturnValue(sizeRoot * sizeRoot);
    }

    @Inject(method = "getTankCapacity", require = 0, at = @At("HEAD"), cancellable = true, remap = false)
    private static void getTankCapacity(long initialCapacity, int tier, CallbackInfoReturnable<Long> cir) {
        if (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches)
            cir.setReturnValue(initialCapacity * (1L << (2 * Math.min(9, tier))));
    }
}
