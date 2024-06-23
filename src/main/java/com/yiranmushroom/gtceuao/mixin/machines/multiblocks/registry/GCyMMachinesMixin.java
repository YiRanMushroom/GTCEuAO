package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.data.machines.GCyMMachines;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING;
import static com.gregtechceu.gtceu.common.data.GCyMBlocks.HEAT_VENT;
import static com.gregtechceu.gtceu.common.data.GCyMRecipeTypes.ALLOY_BLAST_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import java.util.Comparator;
import net.minecraft.core.Direction;

@Mixin(GCyMMachines.class)
public class GCyMMachinesMixin {
    @Mutable
    @Shadow(remap = false)
    @Final
    public static MultiblockMachineDefinition BLAST_ALLOY_SMELTER;

    @Inject(method = "<clinit>", at = @At("RETURN"), remap = false)
    private static void initInj(CallbackInfo ci){
        BLAST_ALLOY_SMELTER = REGISTRATE
            .multiblock("alloy_blast_smelter", CoilWorkableElectricMultiblockMachine::new)
            .langValue("Alloy Blast Smelter")
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                Component.translatable("gtceu.alloy_blast_smelter")))
            .rotationState(RotationState.ALL)
            .recipeType(ALLOY_BLAST_RECIPES)
            .recipeModifier(AORecipeModifier::ebfOverclock)
            .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> FactoryBlockPattern.start()
                .aisle("#XXX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#")
                .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXMXX")
                .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                .aisle("#XSX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#")
                .where('S', controller(blocks(definition.get())))
                .where('X', blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(16)
                    .or(autoAbilities(definition.getRecipeTypes()))
                    .or(Predicates.autoAbilities(true, false, false)))
                .where('C', heatingCoils())
                .where('M', abilities(PartAbility.MUFFLER))
                .where('G', blocks(HEAT_VENT.get()))
                .where('A', air())
                .where('#', any())
                .build())
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
                var builder = MultiblockShapeInfo.builder()
                    .aisle("#XSX#", "#CCC#", "#GGG#", "#CCC#", "#XMX#")
                    .aisle("IXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                    .aisle("XXXXD", "CAAAC", "GAAAG", "CAAAC", "XXHXX")
                    .aisle("FXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                    .aisle("#EXE#", "#CCC#", "#GGG#", "#CCC#", "#XXX#")
                    .where('X', CASING_HIGH_TEMPERATURE_SMELTING.getDefaultState())
                    .where('S', definition, Direction.NORTH)
                    .where('G', HEAT_VENT.getDefaultState())
                    .where('A', Blocks.AIR.defaultBlockState())
                    .where('E', ENERGY_INPUT_HATCH[GTValues.LV], Direction.SOUTH)
                    .where('I', ITEM_IMPORT_BUS[GTValues.LV], Direction.WEST)
                    .where('F', FLUID_IMPORT_HATCH[GTValues.LV], Direction.WEST)
                    .where('D', FLUID_EXPORT_HATCH[GTValues.LV], Direction.EAST)
                    .where('H', MUFFLER_HATCH[GTValues.LV], Direction.UP)
                    .where('M', MAINTENANCE_HATCH, Direction.NORTH);
                GTCEuAPI.HEATING_COILS.entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> entry.getKey().getTier()))
                    .forEach(
                        coil -> shapeInfo.add(builder.shallowCopy().where('C', coil.getValue().get()).build()));
                return shapeInfo;
            })
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"),
                GTCEu.id("block/multiblock/gcym/blast_alloy_smelter"), false)
            .compassSections(GTCompassSections.TIER[IV])
            .compassNodeSelf()
            .register();
    }
}
