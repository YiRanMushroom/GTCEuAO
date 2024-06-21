package com.yiranmushroom.gtceuao.mixin.machines.multiblocks;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.gregtechceu.gtceu.api.GTValues.VA;

@Mixin(FusionReactorMachine.class)
public abstract class FusionReactorMachineMixin extends WorkableElectricMultiblockMachine implements ITieredMachine {
    public FusionReactorMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Inject(method = "recipeModifier", at = @At(value = "RETURN"), remap = false, cancellable = true)
    private static void recipeModifierInj(MetaMachine machine, GTRecipe recipe, CallbackInfoReturnable<GTRecipe> cir) {
        if (cir.getReturnValue() == null) return;
        var result = GTRecipeModifiers.accurateParallel(machine, cir.getReturnValue(),
            gtceuao$getParallelAmount(machine), false);
        cir.setReturnValue(result.getFirst() == recipe ? result.getFirst().copy() : result.getFirst());
    }

    @Unique
    private static int gtceuao$getParallelAmount(MetaMachine machine) {
        return (int) (((FusionReactorMachine) machine).getEnergyContainer().getInputVoltage() / 32768);
    }
}
