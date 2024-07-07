package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.parts;

import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(PartAbility.class)
public class PartAbilityMixin {
    @Final
    @Shadow(remap = false)
    @Mutable
    public static PartAbility SUBSTATION_INPUT_ENERGY;

    @Shadow(remap = false)
    @Final
    public static PartAbility INPUT_ENERGY;

    @Shadow(remap = false)
    @Final
    @Mutable
    public static PartAbility SUBSTATION_OUTPUT_ENERGY;

    @Shadow(remap = false)
    @Final
    public static PartAbility OUTPUT_ENERGY;

    @Mutable
    @Shadow(remap = false)
    @Final
    public static PartAbility OUTPUT_LASER;

    @Mutable
    @Shadow(remap = false)
    @Final
    public static PartAbility INPUT_LASER;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void initInj(CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.machines.substationHatchNormalAbility) {
            SUBSTATION_INPUT_ENERGY = INPUT_ENERGY;
            SUBSTATION_OUTPUT_ENERGY = OUTPUT_ENERGY;
            INPUT_LASER = INPUT_ENERGY;
            OUTPUT_LASER = OUTPUT_ENERGY;
        }
    }
}
