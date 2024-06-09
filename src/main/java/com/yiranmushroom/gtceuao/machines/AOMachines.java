package com.yiranmushroom.gtceuao.machines;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.yiranmushroom.gtceuao.recipes.AORecipeTypes;

import static com.gregtechceu.gtceu.api.GTValues.UV;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_GRATE;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

public class AOMachines {
    public static void init() {
    }

    public static final MultiblockMachineDefinition ADVANCED_PRECISION_ASSEMBLY = REGISTRATE.multiblock("advanced_precision_assembly", WorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.NON_Y_AXIS)
        .recipeType(AORecipeTypes.ADVANCED_PRECISION_ASSEMBLY_Recipe)
        .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK))
        .appearanceBlock(CASING_STEEL_SOLID)
        .pattern(definition -> FactoryBlockPattern.start(BACK, UP, RIGHT) // 7 * 7 * 11
            .aisle("XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX") // first layer
            .aisle("XXXXXXX","RTTTTTR","RAAAAAR", "GTTTTTG", "RAAAAAR", "RTTTTTR", "XXXXXXX").setRepeatable(4)
            // middle:
            .aisle("XXXXXXS","RTTTTTR","RAAAAAR", "GTTTTTG", "RAAAAAR", "RTTTTTR", "XXXXXXX")
            .aisle("XXXXXXX","RTTTTTR","RAAAAAR", "GTTTTTG", "RAAAAAR", "RTTTTTR", "XXXXXXX").setRepeatable(4)
            .aisle("XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX","XXXXXXX") // last layer
            .where('S', Predicates.controller(blocks(definition.getBlock())))
            .where('X', blocks(CASING_STEEL_SOLID.get())
                .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', blocks(CASING_GRATE.get()))
            .where('A', blocks(CASING_ASSEMBLY_CONTROL.get()))
            .where('R', blocks(CASING_LAMINATED_GLASS.get()))
            .where('T', blocks(CASING_ASSEMBLY_LINE.get()))
            .where('#', Predicates.any())
            .build())
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
            GTCEu.id("block/multiblock/advanced_precision_assembly"), false)
        .compassSections(GTCompassSections.TIER[UV])
        .compassNodeSelf()
        .register();
}
