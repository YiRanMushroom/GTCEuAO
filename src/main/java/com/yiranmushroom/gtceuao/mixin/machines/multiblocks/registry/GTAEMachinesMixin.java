package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
//import com.yiranmushroom.gtceuao.recovery.MyMEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.*;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.gregtechceu.gtceu.api.GTValues.EV;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

@Mixin(GTAEMachines.class)
public class GTAEMachinesMixin {
/*    @Final
    @Shadow(remap = false)
    @Mutable
    public static MachineDefinition ITEM_EXPORT_BUS;

    @Final
    @Shadow(remap = false)
    @Mutable
    public static MachineDefinition ITEM_IMPORT_BUS;

    @Final
    @Shadow(remap = false)
    @Mutable
    public static MachineDefinition FLUID_IMPORT_HATCH;

    @Final
    @Shadow(remap = false)
    @Mutable
    public static MachineDefinition FLUID_EXPORT_HATCH;*/
/*
    @Inject(method = "<clinit>", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(CallbackInfo ci){
        ITEM_IMPORT_BUS = REGISTRATE.machine("me_input_bus", MEInputBusPartMachine::new)
            .langValue("ME Stocking Input Bus")
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_ITEMS)
            .overlayTieredHullRenderer("me_item_bus.import")
            .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"))
            .compassNode("item_bus")
            .register();

        ITEM_EXPORT_BUS = REGISTRATE.machine("me_output_bus", MyMEOutputBusPartMachine::new)
            .langValue("ME Output Bus")
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_ITEMS)
            .overlayTieredHullRenderer("me_item_bus.export")
            .tooltips(Component.translatable("gtceu.machine.item_bus.export.tooltip"),
                Component.translatable("gtceu.machine.me.item_export.tooltip"),
                Component.translatable("gtceu.machine.me.export.tooltip"),
                Component.translatable("gtceu.universal.enabled"))
            .compassNode("item_bus")
            .register();

        FLUID_IMPORT_HATCH = REGISTRATE.machine("me_input_hatch", MEInputHatchPartMachine::new)
            .langValue("ME Stocking Input Hatch")
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_FLUIDS)
            .overlayTieredHullRenderer("me_fluid_hatch.import")
            .tooltips(Component.translatable("gtceu.machine.fluid_hatch.import.tooltip"))
            .compassNode("fluid_hatch")
            .register();

        FLUID_EXPORT_HATCH = REGISTRATE.machine("me_output_hatch", MEOutputHatchPartMachine::new)
            .langValue("ME Output Hatch")
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.EXPORT_FLUIDS)
            .overlayTieredHullRenderer("me_fluid_hatch.export")
            .tooltips(Component.translatable("gtceu.machine.fluid_hatch.export.tooltip"))
            .compassNode("fluid_hatch")
            .register();

        ci.cancel();
    }*/
}
