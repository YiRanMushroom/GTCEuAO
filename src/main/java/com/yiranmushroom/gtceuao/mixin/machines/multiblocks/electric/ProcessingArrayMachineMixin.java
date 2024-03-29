/*
package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.TieredWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.yiranmushroom.gtceuao.recovery.ProcessingArrayMachine;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.gtceuao;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.yiranmushroom.gtceuao.recovery.ProcessingArrayMachine.getMachineLimit;

@Mixin(ProcessingArrayMachine.class)
public abstract class ProcessingArrayMachineMixin extends TieredWorkableElectricMultiblockMachine implements IMachineModifyDrops {

    public ProcessingArrayMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    */
/**
     * @author YiRanMushroom
     * @reason Need to modify chance and parallel logic
     *//*

    @Overwrite(remap = false)
    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof ProcessingArrayMachine processingArray && processingArray.machineStorage.storage.getStackInSlot(0).getCount() > 0) {

            int parallelLimit = 0;

            if (AOConfigHolder.INSTANCE.machines.ParallelNeedMorePower){
                parallelLimit = Math.min(
                        processingArray.machineStorage.storage.getStackInSlot(0).getCount(),
                        (int) (processingArray.getMaxVoltage() / RecipeHelper.getInputEUt(recipe))
                );
            } else{
                if (processingArray.getMaxVoltage()>=RecipeHelper.getInputEUt(recipe))
                    parallelLimit = processingArray.machineStorage.storage.getStackInSlot(0).getCount();
                else return null;
            }

            if (parallelLimit <= 0)
                return null;

            parallelLimit*=AOConfigHolder.INSTANCE.machines.PAPMultiplier;

            // apply parallel first
            var parallel = Objects.requireNonNull(GTRecipeModifiers.accurateParallel(
                    machine, recipe, parallelLimit, false
            ));
            int parallelCount = parallel.getB();
            recipe = parallel.getA();

            // apply overclock afterward
//            long maxVoltage = Math.min(processingArray.getOverclockVoltage() * parallelCount, processingArray.getMaxVoltage());
            recipe = RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK, recipe, processingArray.getMaxVoltage());

            return recipe;
        }
        return null;
    }


    @Inject(at = @At("HEAD"), method = "getCasingState", remap = false, cancellable = true)
    private static void getCasingState(int tier, CallbackInfoReturnable<Block> cir) {
        if (tier <= GTValues.IV) {
            cir.setReturnValue(GTBlocks.CASING_STEEL_SOLID.get());
        } else {
            cir.setReturnValue(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get());
        }
    }

    @Inject(at = @At("HEAD"), method = "getOutputLimits", remap = false, cancellable = true)
    private void getOutputLimits(CallbackInfoReturnable<Map<RecipeCapability<?>, Integer>> cir) {
        cir.setReturnValue(super.getOutputLimits());
    }

    @Inject(at = @At("HEAD"), method = "getTier", remap = false, cancellable = true)
    private void getTier(CallbackInfoReturnable<Integer> cir) {
        if (AOConfigHolder.INSTANCE.machines.PAIgnoreTier) {
            cir.setReturnValue((int) GTUtil.getTierByVoltage(getMaxVoltage()));
        }
    }

    @Inject(at = @At("HEAD"), method = "getOverclockTier", remap = false, cancellable = true)
    private void getOverclockTier(CallbackInfoReturnable<Integer> cir) {
        if (AOConfigHolder.INSTANCE.machines.PAIgnoreTier) {
            cir.setReturnValue((int) GTUtil.getTierByVoltage(getMaxVoltage()));
        }
    }
}
*/
