package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import appeng.core.definitions.AEEntities;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
//import com.yiranmushroom.gtceuao.recovery.MyMEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.*;
import com.yiranmushroom.gtceuao.recipes.machine.AERecipes;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.gregtechceu.gtceu.api.GTValues.EV;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

@Mixin(GTAEMachines.class)
public class GTAEMachinesMixin {

    @Inject(method = "init", at = @At("HEAD"), remap = false)
    private static void init(CallbackInfo ci){
        AERecipes.AE2BusAndHatchLoaded = true;
    }
}
