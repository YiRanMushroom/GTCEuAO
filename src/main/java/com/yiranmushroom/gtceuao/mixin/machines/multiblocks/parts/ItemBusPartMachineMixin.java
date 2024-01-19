package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.parts;

import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBusPartMachine.class)
public class ItemBusPartMachineMixin {
    @Inject(method = "getInventorySize", at = @At("HEAD"), cancellable = true, remap = false)
    private void getInventorySize(CallbackInfoReturnable<Integer> cir) {
        if (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches) {
            int sizeRoot = Math.min(1 + 2 * Math.min(9, ((ItemBusPartMachine) (Object) this).getTier()), 10);
            cir.setReturnValue(sizeRoot * sizeRoot);
        }

    }
}
