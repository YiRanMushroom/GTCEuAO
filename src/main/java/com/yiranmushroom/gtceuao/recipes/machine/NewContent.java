package com.yiranmushroom.gtceuao.recipes.machine;

import com.gregtechceu.gtceu.data.recipe.CustomTags;
import net.minecraft.data.recipes.FinishedRecipe;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTItems.FIELD_GENERATOR_ZPM;
import static com.gregtechceu.gtceu.common.data.GTMachines.ASSEMBLY_LINE;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.yiranmushroom.gtceuao.machines.AOMachines.ADVANCED_PRECISION_ASSEMBLY;

import java.util.function.Consumer;

public class NewContent {
    public static void register(Consumer<FinishedRecipe> provider) {
        ASSEMBLY_LINE_RECIPES.recipeBuilder("advanced_precision_assembly_machine")
            .inputItems(ASSEMBLY_LINE, 4)
            .inputItems(CustomTags.UV_CIRCUITS, 16)
            .inputItems(CONVEYOR_MODULE_ZPM, 16)
            .inputItems(ELECTRIC_PUMP_ZPM, 16)
            .inputItems(ROBOT_ARM_ZPM, 4)
            .inputItems(SENSOR_ZPM, 4)
            .inputItems(EMITTER_ZPM, 4)
            .inputItems(FIELD_GENERATOR_ZPM, 1)
            .inputFluids(Naquadria.getFluid(144 * 16))
            .inputFluids(Polybenzimidazole.getFluid(144 * 64))
            .inputFluids(Lubricant.getFluid(144 * 16))
            .inputFluids(Polytetrafluoroethylene.getFluid(144 * 64))
            .outputItems(ADVANCED_PRECISION_ASSEMBLY)
            .EUt(VA[ZPM]).duration(20 * 60 * 60).save(provider);
    }
}
