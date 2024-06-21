package com.yiranmushroom.gtceuao.mixin.gregiceng;

import com.epimorphismmc.gregiceng.common.data.GEMachines;
import com.epimorphismmc.gregiceng.common.machine.multiblock.part.BufferPartMachine;
import com.epimorphismmc.gregiceng.config.GEConfigHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.epimorphismmc.gregiceng.common.data.GEMachines.registerTieredGEMachines;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.MULTI_HATCH_TIERS;

@Mixin(value = GEMachines.class, remap = false)
public class GEMachinesMixin {
    // wait for the next update
/*    @Final
    @Mutable
    @Shadow(remap = false)
    public static MachineDefinition[] INPUT_BUFFER;

    @Inject(method = "<clinit>", at = @At("RETURN"), remap = false)
    private static void clinitInj(CallbackInfo ci){
        INPUT_BUFFER = registerTieredGEMachines(
            "input_buffer",
            (holder, tier) -> new BufferPartMachine(holder, tier, IO.IN),
            (tier, builder) -> builder
                .langValue(VNF[tier] + " Input Buffer")
                .rotationState(RotationState.ALL)
                .abilities(
                    GEConfigHolder.INSTANCE.enableMoreAbility
                        ? INPUT_BUFFER_ABILITIES
                        : new PartAbility[] {PartAbility.IMPORT_ITEMS})
                .overlayTieredHullRenderer("buffer.import")
                .tooltips(
                    Component.translatable("block.gregiceng.input_buffer.desc"),
                    Component.translatable(
                        "gtceu.universal.tooltip.item_storage_capacity",
                        (1 + Math.min(9, tier)) * (1 + Math.min(9, tier))),
                    Component.translatable(
                        "gtceu.universal.tooltip.fluid_storage_capacity_mult",
                        1 + Math.min(9, tier),
                        FluidHatchPartMachine.getTankCapacity(
                            BufferPartMachine.INITIAL_TANK_CAPACITY, tier)),
                    Component.translatable("gtceu.universal.enabled"))
                .register(),
            MULTI_HATCH_TIERS);
    }*/

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/MachineBuilder;tier(I)Lcom/gregtechceu/gtceu/api/registry/registrate/MachineBuilder;"), index = 0, remap = false)
    private static int modifyTier(int tier) {
        if (tier == LuV && AOConfigHolder.INSTANCE.recipes.AE2RecipeSupport) return MV;
        return tier;
    }
}
