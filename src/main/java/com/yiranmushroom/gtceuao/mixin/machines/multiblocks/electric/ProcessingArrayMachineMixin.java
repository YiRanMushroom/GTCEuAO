package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.TieredWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.ProcessingArrayMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.gregtechceu.gtceu.common.machine.multiblock.electric.ProcessingArrayMachine.getMachineLimit;

@Mixin(ProcessingArrayMachine.class)
public abstract class ProcessingArrayMachineMixin extends TieredWorkableElectricMultiblockMachine implements IMachineModifyDrops{

    public ProcessingArrayMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof ProcessingArrayMachine processingArray && processingArray.machineStorage.storage.getStackInSlot(0).getCount() > 0) {
            if (RecipeHelper.getRecipeEUtTier(recipe) > processingArray.getTier()) {
                return null;
            }

            var limit = processingArray.machineStorage.storage.getStackInSlot(0).getCount();
            // apply parallel first
            recipe = GTRecipeModifiers.accurateParallel(machine, recipe, AOConfigHolder.INSTANCE.machines.PAPMultiplier * Math.min(limit, getMachineLimit(machine.getDefinition().getTier())), false).getA();
            // apply overclock later
            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, processingArray.getOverclockVoltage());
            return recipe;
        }
        return null;
    }


    @Inject(at = @At("HEAD"), method = "getCasingState",  remap = false, cancellable = true)
    private static void getCasingState(int tier, CallbackInfoReturnable<Block> cir) {
        if (tier <= GTValues.IV) {
            cir.setReturnValue(GTBlocks.CASING_STEEL_SOLID.get());
        } else {
            cir.setReturnValue(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get());
        }
    }

}
