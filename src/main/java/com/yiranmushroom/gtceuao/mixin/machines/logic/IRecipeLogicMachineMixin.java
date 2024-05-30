package com.yiranmushroom.gtceuao.mixin.machines.logic;

import com.gregtechceu.gtceu.api.capability.ICleanroomReceiver;
import com.gregtechceu.gtceu.api.capability.IWorkable;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.IVoidable;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IRecipeLogicMachine.class)
public interface IRecipeLogicMachineMixin extends IRecipeCapabilityHolder, IMachineFeature, IWorkable, ICleanroomReceiver, IVoidable {
    /**
     * @author Yiran Mushroom
     * @reason Add logic to try to modify recipe even if it succeeded
     */

    @Overwrite(remap = false)
    default boolean alwaysTryModifyRecipe() {
        if (AOConfigHolder.INSTANCE.machines.notLazyRecipeModifier)
            return true;
        else
            return this.self().getDefinition().isAlwaysTryModifyRecipe();
    }
}
