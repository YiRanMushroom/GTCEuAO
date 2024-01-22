package com.yiranmushroom.gtceuao.recipes.machine;

import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.lowdragmc.lowdraglib.Platform;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.frameGt;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.gear;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTItems.ELECTRIC_PUMP_LuV;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Osmiridium;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

public class MiscRecipes {
    public static void register(Consumer<FinishedRecipe> provider) {

        if (ConfigHolder.INSTANCE.machines.doBedrockOres || Platform.isDevEnv()) {
            ASSEMBLER_RECIPES.recipeBuilder("fluid_drill_mv")
                    .inputItems(HULL[MV])
                    .inputItems(frameGt, Aluminium, 4)
                    .inputItems(CustomTags.MV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_MV, 4)
                    .inputItems(ELECTRIC_PUMP_MV, 4)
                    .inputItems(gear, VanadiumSteel, 4)
                    .circuitMeta(2)
                    .outputItems(FLUID_DRILLING_RIG[MV])
                    .duration(400).EUt(VA[MV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("fluid_drill_hv")
                    .inputItems(HULL[HV])
                    .inputItems(frameGt, StainlessSteel, 4)
                    .inputItems(CustomTags.HV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_HV, 4)
                    .inputItems(ELECTRIC_PUMP_HV, 4)
                    .inputItems(gear, BlueSteel, 4)
                    .circuitMeta(2)
                    .outputItems(FLUID_DRILLING_RIG[HV])
                    .duration(400).EUt(VA[EV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("fluid_drill_ev")
                    .inputItems(HULL[EV])
                    .inputItems(frameGt, Titanium, 4)
                    .inputItems(CustomTags.EV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_EV, 4)
                    .inputItems(ELECTRIC_PUMP_EV, 4)
                    .inputItems(gear, Ultimet, 4)
                    .circuitMeta(2)
                    .outputItems(FLUID_DRILLING_RIG[EV])
                    .duration(400).EUt(VA[IV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("mv_fluid_drilling_rig")
                    .inputItems(HULL[MV])
                    .inputItems(frameGt, Aluminium, 4)
                    .inputItems(CustomTags.MV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_MV, 4)
                    .inputItems(ELECTRIC_PUMP_MV, 4)
                    .inputItems(gear, VanadiumSteel, 4)
                    .circuitMeta(2)
                    .outputItems(FLUID_DRILLING_RIG[MV])
                    .duration(400).EUt(VA[MV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("hv_fluid_drilling_rig")
                    .inputItems(HULL[HV])
                    .inputItems(frameGt, StainlessSteel, 4)
                    .inputItems(CustomTags.HV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_HV, 4)
                    .inputItems(ELECTRIC_PUMP_HV, 4)
                    .inputItems(gear, BlueSteel, 4)
                    .circuitMeta(2)
                    .outputItems(FLUID_DRILLING_RIG[HV])
                    .duration(400).EUt(VA[EV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("ev_fluid_drilling_rig")
                    .inputItems(HULL[EV])
                    .inputItems(frameGt, Titanium, 4)
                    .inputItems(CustomTags.EV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_EV, 4)
                    .inputItems(ELECTRIC_PUMP_EV, 4)
                    .inputItems(gear, Ultimet, 4)
                    .circuitMeta(2)
                    .outputItems(FLUID_DRILLING_RIG[EV])
                    .duration(400).EUt(VA[IV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("mv_bedrock_ore_miner")
                    .inputItems(HULL[MV])
                    .inputItems(frameGt, Aluminium, 4)
                    .inputItems(CustomTags.MV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_MV, 4)
                    .inputItems(CONVEYOR_MODULE_MV, 4)
                    .inputItems(gear, VanadiumSteel, 4)
                    .circuitMeta(2)
                    .outputItems(BEDROCK_ORE_MINER[MV])
                    .duration(400).EUt(VA[MV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("hv_bedrock_ore_miner")
                    .inputItems(HULL[HV])
                    .inputItems(frameGt, StainlessSteel, 4)
                    .inputItems(CustomTags.HV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_HV, 4)
                    .inputItems(CONVEYOR_MODULE_HV, 4)
                    .inputItems(gear, BlueSteel, 4)
                    .circuitMeta(2)
                    .outputItems(BEDROCK_ORE_MINER[HV])
                    .duration(400).EUt(VA[HV]).save(provider);

            ASSEMBLER_RECIPES.recipeBuilder("ev_bedrock_ore_miner")
                    .inputItems(HULL[EV])
                    .inputItems(frameGt, Titanium, 4)
                    .inputItems(CustomTags.EV_CIRCUITS, 4)
                    .inputItems(ELECTRIC_MOTOR_EV, 4)
                    .inputItems(CONVEYOR_MODULE_EV, 4)
                    .inputItems(gear, Ultimet, 4)
                    .circuitMeta(2)
                    .outputItems(BEDROCK_ORE_MINER[EV])
                    .duration(400).EUt(VA[EV]).save(provider);
        }

    }
}
