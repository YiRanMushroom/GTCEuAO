package com.yiranmushroom.gtceuao.mixin.machines.multiblocks;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.generator.LargeTurbineMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Mixin(LargeTurbineMachine.class)
public abstract class LargeTurbineMachineMixin extends WorkableElectricMultiblockMachine implements ITieredMachine {
    @Shadow(remap = false)
    public abstract int getTier();

    public LargeTurbineMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Unique
    private static Method getRotorHolderMethod = null;

    @Unique
    private static Method boostProductionMethod = null;

    @Unique
    private static Field excessVoltageField = null;

    static {
        try {
            getRotorHolderMethod = LargeTurbineMachine.class.getDeclaredMethod("getRotorHolder");
            getRotorHolderMethod.setAccessible(true);

            boostProductionMethod = LargeTurbineMachine.class.getDeclaredMethod("boostProduction", long.class);
            boostProductionMethod.setAccessible(true);

            excessVoltageField = LargeTurbineMachine.class.getDeclaredField("excessVoltage");
            excessVoltageField.setAccessible(true);

        } catch (NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "getOverclockVoltage", at = @At("HEAD"), cancellable = true, remap = false)
    public void getOverclockVoltage(CallbackInfoReturnable<Long> cir) {
        if (AOConfigHolder.INSTANCE.machines.buffTurbines) {
            cir.setReturnValue(super.getOverclockVoltage());
        }
    }

    @Inject(method = "recipeModifier", at = @At("HEAD"), cancellable = true, remap = false)
    private static void recipeModifier(MetaMachine machine, GTRecipe recipe, CallbackInfoReturnable<GTRecipe> cir) throws InvocationTargetException, IllegalAccessException {
        if (AOConfigHolder.INSTANCE.machines.buffTurbines) {
            if (!(machine instanceof LargeTurbineMachine turbineMachine))
                cir.setReturnValue(null);
            else {
                var rotorHolder = (IRotorHolderMachine) getRotorHolderMethod.invoke(turbineMachine);

                var EUt = RecipeHelper.getOutputEUt(recipe);

                if (rotorHolder == null || EUt <= 0)
                    cir.setReturnValue(null);

                var turbineMaxVoltage = (int) turbineMachine.getOverclockVoltage();
                if (excessVoltageField.getInt(turbineMachine) >= turbineMaxVoltage) {
                    excessVoltageField.setInt(turbineMachine, (int) (excessVoltageField.getLong(turbineMachine) - turbineMaxVoltage));
                    cir.setReturnValue(null);
                }

                double holderEfficiency = rotorHolder.getTotalEfficiency() / 100.0;

                if (holderEfficiency <= 1) holderEfficiency = 1;

                // get the amount of parallel required to match the desired output voltage
                var maxParallel = (int) ((turbineMaxVoltage - excessVoltageField.getInt(turbineMachine)) / (EUt * holderEfficiency));

                excessVoltageField.setInt(turbineMachine, (int) (excessVoltageField.getInt(turbineMachine) + (maxParallel * EUt * holderEfficiency - turbineMaxVoltage)));

                var parallelResult = GTRecipeModifiers.fastParallel(turbineMachine, recipe, Math.max(1, maxParallel), false);
                recipe = parallelResult.getFirst() == recipe ? recipe.copy() : parallelResult.getFirst();

                long eut = (long) boostProductionMethod.invoke(turbineMachine, (long) (EUt * holderEfficiency * parallelResult.getSecond()));

                recipe.tickOutputs.put(EURecipeCapability.CAP, List.of(new Content(eut, 1.0f, 0.0f, null, null)));

                cir.setReturnValue(recipe);
            }
        }
    }
}
