package com.yiranmushroom.gtceuao.recipes.machine;

import net.minecraft.data.recipes.FinishedRecipe;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.ASSEMBLY_LINE;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.yiranmushroom.gtceuao.machines.AOMachines.ADVANCED_PRECISION_ASSEMBLY;

import java.util.function.Consumer;

public class NewContent {
    public static void register(Consumer<FinishedRecipe> provider) {
        ASSEMBLY_LINE_RECIPES.recipeBuilder("advanced_precision_assembly_machine")
            .inputItems(ASSEMBLY_LINE, 4)
            .inputItems(WETWARE_MAINFRAME_UHV, 16)
            .inputItems(CONVEYOR_MODULE_UV, 16)
            .inputItems(ELECTRIC_PUMP_UV, 16)
            .inputItems(ROBOT_ARM_UV, 4)
            .inputItems(SENSOR_UV, 4)
            .inputItems(EMITTER_UV, 4)
            .inputItems(FIELD_GENERATOR_UV, 1)
            .inputFluids(Neutronium.getFluid(144 * 16))
            .inputFluids(Polybenzimidazole.getFluid(144 * 64))
            .inputFluids(Americium.getFluid(144 * 16))
            .inputFluids(Trinium.getFluid(144 * 64))
            .outputItems(ADVANCED_PRECISION_ASSEMBLY)
            .EUt(VA[UHV]).duration(20 * 60 * 5).save(provider);
    }
}
