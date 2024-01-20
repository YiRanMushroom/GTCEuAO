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
import com.gregtechceu.gtceu.common.machine.multiblock.electric.ProcessingArrayMachine;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.gregtechceu.gtceu.common.machine.multiblock.electric.ProcessingArrayMachine.getMachineLimit;

@Mixin(ProcessingArrayMachine.class)
public abstract class ProcessingArrayMachineMixin extends TieredWorkableElectricMultiblockMachine implements IMachineModifyDrops {

    public ProcessingArrayMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    /**
     * @author YiRanMushroom
     * @reason Need to modify chance and parallel logic
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

            if (AOConfigHolder.INSTANCE.machines.PAHasOPChance) {
                var copied = recipe.copy();
                for (Content content : copied.getOutputContents(ItemRecipeCapability.CAP)) {
                    content.chance = 1;
                }
                return copied;
            }

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

    @Inject(at = @At("HEAD"), method = "getMaxOverclockTier", remap = false, cancellable = true)
    private void getMaxOverclockTier(CallbackInfoReturnable<Integer> cir) {
        if (AOConfigHolder.INSTANCE.machines.PAIgnoreTier) {
            cir.setReturnValue((int) GTUtil.getTierByVoltage(getMaxVoltage()));
        }
    }
}
