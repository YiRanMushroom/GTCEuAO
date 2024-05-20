package com.yiranmushroom.gtceuao.recipes.machine;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTItems.HIGHLY_ADVANCED_SOC_WAFER;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class CircuitRecipes {
    public static void register(Consumer<FinishedRecipe> provider) {
        if (AOConfigHolder.INSTANCE.recipes.expensiveWafersHaveHighOutput)
            registerHighOutputWafers(provider);

        if (AOConfigHolder.INSTANCE.recipes.easierCrystalProcessors)
            registerCrystalProcessors(provider);

        if (AOConfigHolder.INSTANCE.recipes.circuitsHaveHigherOutput)
            registerCircuits(provider);

        if (AOConfigHolder.INSTANCE.recipes.easierBoardRecipes)
            registerBoards(provider);

    }

    private static void registerBoards(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, "coated_board", COATED_BOARD.asStack(6),
                "RRR", "PPP", "RRR",
                'R', STICKY_RESIN.asStack(),
                'P', new UnificationEntry(plate, Wood));

        VanillaRecipeHelper.addShapelessRecipe(provider, "coated_board_1x", COATED_BOARD.asStack(2),
                new UnificationEntry(plate, Wood),
                STICKY_RESIN.asStack(),
                STICKY_RESIN.asStack());

        VanillaRecipeHelper.addShapedRecipe(provider, "basic_circuit_board", BASIC_CIRCUIT_BOARD.asStack(2),
                "WWW", "WBW", "WWW",
                'W', new UnificationEntry(wireGtSingle, Copper),
                'B', COATED_BOARD.asStack());

        // Basic Circuit Board
        ASSEMBLER_RECIPES.recipeBuilder("basic_circuit_board")
                .inputItems(foil, Copper, 4)
                .inputItems(plate, Wood)
                .inputFluids(Glue.getFluid(100))
                .outputItems(BASIC_CIRCUIT_BOARD, 2)
                .duration(200).EUt(VA[ULV]).save(provider);

        // Phenolic Board
        ASSEMBLER_RECIPES.recipeBuilder("phenolic_board")
                .inputItems(dust, Wood)
                .circuitMeta(1)
                .inputFluids(Glue.getFluid(50))
                .outputItems(PHENOLIC_BOARD, 2)
                .duration(150).EUt(VA[LV]).save(provider);

        // Good Circuit Board
        VanillaRecipeHelper.addShapedRecipe(provider, "good_circuit_board", GOOD_CIRCUIT_BOARD.asStack(),
                "WWW", "WBW", "WWW",
                'W', new UnificationEntry(wireGtSingle, Silver),
                'B', PHENOLIC_BOARD.asStack(2));

        CHEMICAL_RECIPES.recipeBuilder("good_circuit_board_persulfate").EUt(VA[LV]).duration(300)
                .inputItems(foil, Silver, 4)
                .inputItems(PHENOLIC_BOARD)
                .inputFluids(SodiumPersulfate.getFluid(200))
                .outputItems(GOOD_CIRCUIT_BOARD, 2)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("good_circuit_board_iron3").EUt(VA[LV]).duration(300)
                .inputItems(foil, Silver, 4)
                .inputItems(PHENOLIC_BOARD)
                .inputFluids(Iron3Chloride.getFluid(100))
                .outputItems(GOOD_CIRCUIT_BOARD, 2)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("good_circuit_board_chlorine").EUt(VA[LV]).duration(300)
                .inputItems(foil, Silver, 4)
                .inputItems(PHENOLIC_BOARD)
                .inputFluids(Chlorine.getFluid(200))
                .outputItems(GOOD_CIRCUIT_BOARD, 2)
                .save(provider);

        // Plastic Board
        CHEMICAL_RECIPES.recipeBuilder("plastic_board_polyethylene").duration(500).EUt(10)
                .inputItems(plate, Polyethylene)
                .inputItems(foil, Copper, 4)
                .inputFluids(SulfuricAcid.getFluid(250))
                .outputItems(PLASTIC_BOARD, 2)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("plastic_board_pvc").duration(500).EUt(10)
                .inputItems(plate, PolyvinylChloride)
                .inputItems(foil, Copper, 4)
                .inputFluids(SulfuricAcid.getFluid(250))
                .outputItems(PLASTIC_BOARD, 4)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("plastic_board_ptfe").duration(500).EUt(10)
                .inputItems(plate, Polytetrafluoroethylene)
                .inputItems(foil, Copper, 4)
                .inputFluids(SulfuricAcid.getFluid(250))
                .outputItems(PLASTIC_BOARD, 8)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("plastic_board_pbi").duration(500).EUt(10)
                .inputItems(plate, Polybenzimidazole)
                .inputItems(foil, Copper, 4)
                .inputFluids(SulfuricAcid.getFluid(250))
                .outputItems(PLASTIC_BOARD, 16)
                .save(provider);

        // Plastic Circuit Board
        CHEMICAL_RECIPES.recipeBuilder("plastic_circuit_board_persulfate").duration(600).EUt(VA[LV])
                .inputItems(PLASTIC_BOARD)
                .inputItems(foil, Copper, 6)
                .inputFluids(SodiumPersulfate.getFluid(500))
                .outputItems(PLASTIC_CIRCUIT_BOARD, 2)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("plastic_circuit_board_iron3").duration(600).EUt(VA[LV])
                .inputItems(PLASTIC_BOARD)
                .inputItems(foil, Copper, 6)
                .inputFluids(Iron3Chloride.getFluid(250))
                .outputItems(PLASTIC_CIRCUIT_BOARD, 2)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("plastic_circuit_board_chlorine").duration(600).EUt(VA[LV])
                .inputItems(PLASTIC_BOARD)
                .inputItems(foil, Copper, 6)
                .inputFluids(Chlorine.getFluid(500))
                .outputItems(PLASTIC_CIRCUIT_BOARD, 2)
                .save(provider);

        // Epoxy Board
        CHEMICAL_RECIPES.recipeBuilder("epoxy_board").duration(600).EUt(VA[LV])
                .inputItems(plate, Epoxy)
                .inputItems(foil, Gold, 8)
                .inputFluids(SulfuricAcid.getFluid(500))
                .outputItems(EPOXY_BOARD, 2)
                .save(provider);

        // Advanced Circuit Board
        CHEMICAL_RECIPES.recipeBuilder("advanced_circuit_board_persulfate").duration(900).EUt(VA[LV])
                .inputItems(EPOXY_BOARD)
                .inputItems(foil, Electrum, 8)
                .inputFluids(SodiumPersulfate.getFluid(1000))
                .outputItems(ADVANCED_CIRCUIT_BOARD, 2)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("advanced_circuit_board_iron3").duration(900).EUt(VA[LV])
                .inputItems(EPOXY_BOARD)
                .inputItems(foil, Electrum, 8)
                .inputFluids(Iron3Chloride.getFluid(500))
                .outputItems(ADVANCED_CIRCUIT_BOARD, 2)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("advanced_circuit_board_chlorine").duration(900).EUt(VA[LV])
                .inputItems(EPOXY_BOARD)
                .inputItems(foil, Electrum, 8)
                .inputFluids(Chlorine.getFluid(1000))
                .outputItems(ADVANCED_CIRCUIT_BOARD, 2)
                .save(provider);

        // Fiber Reinforced Epoxy Board
        CHEMICAL_BATH_RECIPES.recipeBuilder("reinforced_epoxy_sheet_glass").duration(240).EUt(16)
                .inputItems(wireFine, BorosilicateGlass)
                .inputFluids(Epoxy.getFluid(L))
                .outputItems(plate, ReinforcedEpoxyResin, 2)
                .save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder("reinforced_epoxy_sheet_carbon_fibers").duration(240).EUt(16)
                .inputItems(CARBON_FIBERS)
                .inputFluids(Epoxy.getFluid(L))
                .outputItems(plate, ReinforcedEpoxyResin, 2)
                .save(provider);

        // Borosilicate Glass Recipes
        EXTRUDER_RECIPES.recipeBuilder("borosilicate_glass_fine_wire").duration(160).EUt(96)
                .inputItems(ingot, BorosilicateGlass)
                .notConsumable(SHAPE_EXTRUDER_WIRE)
                .outputItems(wireFine, BorosilicateGlass, 16)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("fiber_board").duration(500).EUt(10)
                .inputItems(plate, ReinforcedEpoxyResin)
                .inputItems(foil, AnnealedCopper, 8)
                .inputFluids(SulfuricAcid.getFluid(125))
                .outputItems(FIBER_BOARD, 2)
                .save(provider);

        // Extreme Circuit Board
        CHEMICAL_RECIPES.recipeBuilder("extreme_circuit_board_persulfate").duration(1200).EUt(VA[LV])
                .inputItems(FIBER_BOARD)
                .inputItems(foil, AnnealedCopper, 12)
                .inputFluids(SodiumPersulfate.getFluid(2000))
                .outputItems(EXTREME_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("extreme_circuit_board_iron3").duration(1200).EUt(VA[LV])
                .inputItems(FIBER_BOARD)
                .inputItems(foil, AnnealedCopper, 12)
                .inputFluids(Iron3Chloride.getFluid(1000))
                .outputItems(EXTREME_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("extreme_circuit_board_chlorine").duration(1200).EUt(VA[LV])
                .inputItems(FIBER_BOARD)
                .inputItems(foil, AnnealedCopper, 12)
                .inputFluids(Chlorine.getFluid(2000))
                .outputItems(EXTREME_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // Multi-Layer Fiber Reinforced Epoxy Board
        CHEMICAL_RECIPES.recipeBuilder("multilayer_fiber_board").duration(500).EUt(VA[HV])
                .inputItems(FIBER_BOARD, 2)
                .inputItems(foil, Palladium, 8)
                .inputFluids(SulfuricAcid.getFluid(500))
                .outputItems(MULTILAYER_FIBER_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // Elite Circuit Board
        CHEMICAL_RECIPES.recipeBuilder("elite_circuit_board_persulfate").duration(1500).EUt(VA[MV])
                .inputItems(MULTILAYER_FIBER_BOARD)
                .inputItems(foil, Platinum, 8)
                .inputFluids(SodiumPersulfate.getFluid(4000))
                .outputItems(ELITE_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("elite_circuit_board_iron3").duration(1500).EUt(VA[MV])
                .inputItems(MULTILAYER_FIBER_BOARD)
                .inputItems(foil, Platinum, 8)
                .inputFluids(Iron3Chloride.getFluid(2000))
                .outputItems(ELITE_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("elite_circuit_board_chlorine").duration(1500).EUt(VA[MV])
                .inputItems(MULTILAYER_FIBER_BOARD)
                .inputItems(foil, Platinum, 8)
                .inputFluids(Chlorine.getFluid(8000))
                .outputItems(ELITE_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // Wetware Board

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("wetware_board").duration(1200).EUt(VA[LuV])
                .inputItems(MULTILAYER_FIBER_BOARD, 16)
                .notConsumable(PETRI_DISH)
                .notConsumable(ELECTRIC_PUMP_LuV)
                .notConsumable(SENSOR_IV)
                .inputItems(CustomTags.IV_CIRCUITS, 2)
                .inputItems(foil, NiobiumTitanium, 16)
                .inputFluids(SterileGrowthMedium.getFluid(4000))
                .outputItems(WETWARE_BOARD, 32)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("wetware_circuit_board_persulfate").duration(1800).EUt(VA[HV])
                .inputItems(WETWARE_BOARD)
                .inputItems(foil, NiobiumTitanium, 32)
                .inputFluids(SodiumPersulfate.getFluid(10000))
                .outputItems(WETWARE_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("wetware_circuit_board_iron3").duration(1800).EUt(VA[HV])
                .inputItems(WETWARE_BOARD)
                .inputItems(foil, NiobiumTitanium, 32)
                .inputFluids(Iron3Chloride.getFluid(5000))
                .outputItems(WETWARE_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("wetware_circuit_board_chlorine").duration(1800).EUt(VA[HV])
                .inputItems(WETWARE_BOARD)
                .inputItems(foil, NiobiumTitanium, 32)
                .inputFluids(Chlorine.getFluid(20000))
                .outputItems(WETWARE_CIRCUIT_BOARD, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }


    private static void registerCircuits(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, "electronic_circuit_lv", ELECTRONIC_CIRCUIT_LV.asStack(4),
                "RPR", "VBV", "CCC",
                'R', RESISTOR.asStack(),
                'P', new UnificationEntry(plate, Steel),
                'V', VACUUM_TUBE.asStack(),
                'B', BASIC_CIRCUIT_BOARD.asStack(),
                'C', new UnificationEntry(cableGtSingle, Tin));

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("electronic_circuit_lv").EUt(16).duration(200)
                .inputItems(BASIC_CIRCUIT_BOARD)
                .inputItems(CustomTags.RESISTORS, 2)
                .inputItems(wireGtSingle, Tin, 2)
                .inputItems(CustomTags.ULV_CIRCUITS, 2)
                .outputItems(ELECTRONIC_CIRCUIT_LV, 8)
                .save(provider);

        // MV
        VanillaRecipeHelper.addShapedRecipe(provider, "electronic_circuit_mv", ELECTRONIC_CIRCUIT_MV.asStack(2),
                "DPD", "CBC", "WCW",
                'W', new UnificationEntry(wireGtSingle, Copper),
                'P', new UnificationEntry(plate, Steel),
                'C', ELECTRONIC_CIRCUIT_LV.asStack(),
                'B', GOOD_CIRCUIT_BOARD.asStack(),
                'D', DIODE.asStack());

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("electronic_circuit_mv").EUt(VA[LV]).duration(300)
                .inputItems(GOOD_CIRCUIT_BOARD)
                .inputItems(CustomTags.LV_CIRCUITS, 2)
                .inputItems(CustomTags.DIODES, 2)
                .inputItems(wireGtSingle, Copper, 2)
                .outputItems(ELECTRONIC_CIRCUIT_MV, 4)
                .save(provider);

        // T2: Integrated ==============================================================================================

        // LV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("integrated_circuit_lv").EUt(16).duration(200)
                .inputItems(BASIC_CIRCUIT_BOARD)
                .inputItems(INTEGRATED_LOGIC_CIRCUIT)
                .inputItems(CustomTags.RESISTORS, 2)
                .inputItems(CustomTags.DIODES, 2)
                .inputItems(wireFine, Copper, 2)
                .inputItems(bolt, Tin, 2)
                .outputItems(INTEGRATED_CIRCUIT_LV, 12)
                .save(provider);

        // MV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("integrated_circuit_mv").EUt(24).duration(400)
                .inputItems(GOOD_CIRCUIT_BOARD)
                .inputItems(INTEGRATED_CIRCUIT_LV, 2)
                .inputItems(CustomTags.RESISTORS, 2)
                .inputItems(CustomTags.DIODES, 2)
                .inputItems(wireFine, Gold, 4)
                .inputItems(bolt, Silver, 4)
                .outputItems(INTEGRATED_CIRCUIT_MV, 4)
                .save(provider);

        // HV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("integrated_circuit_hv").EUt(VA[LV]).duration(800)
                .inputItems(INTEGRATED_CIRCUIT_MV, 2)
                .inputItems(INTEGRATED_LOGIC_CIRCUIT, 2)
                .inputItems(RANDOM_ACCESS_MEMORY, 2)
                .inputItems(CustomTags.TRANSISTORS, 4)
                .inputItems(wireFine, Electrum, 8)
                .inputItems(bolt, AnnealedCopper, 8)
                .outputItems(INTEGRATED_CIRCUIT_HV, 2)
                .save(provider);

        // T2.5: Misc ==================================================================================================

        // NAND Chip ULV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nand_chip_ulv_good_board").EUt(VA[MV]).duration(300)
                .inputItems(GOOD_CIRCUIT_BOARD)
                .inputItems(SIMPLE_SYSTEM_ON_CHIP)
                .inputItems(bolt, RedAlloy, 2)
                .inputItems(wireFine, Tin, 2)
                .outputItems(NAND_CHIP_ULV, 16)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nand_chip_ulv_plastic_board").EUt(VA[MV]).duration(300)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(SIMPLE_SYSTEM_ON_CHIP)
                .inputItems(bolt, RedAlloy, 2)
                .inputItems(wireFine, Tin, 2)
                .outputItems(NAND_CHIP_ULV, 24)
                .save(provider);

        // Microprocessor LV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("microprocessor_lv").EUt(60).duration(200)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(CENTRAL_PROCESSING_UNIT)
                .inputItems(CustomTags.RESISTORS, 2)
                .inputItems(CustomTags.CAPACITORS, 2)
                .inputItems(CustomTags.TRANSISTORS, 2)
                .inputItems(wireFine, Copper, 2)
                .outputItems(MICROPROCESSOR_LV, 12)
                .save(provider);

        // Microprocessor LV SoC
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("microprocessor_lv_soc").EUt(600).duration(50)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(SYSTEM_ON_CHIP)
                .inputItems(wireFine, Copper, 2)
                .inputItems(bolt, Tin, 2)
                .outputItems(MICROPROCESSOR_LV, 24)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // T3: Processor ===============================================================================================

        // MV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("processor_mv").EUt(60).duration(200)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(CENTRAL_PROCESSING_UNIT)
                .inputItems(CustomTags.RESISTORS, 4)
                .inputItems(CustomTags.CAPACITORS, 4)
                .inputItems(CustomTags.TRANSISTORS, 4)
                .inputItems(wireFine, RedAlloy, 4)
                .outputItems(PROCESSOR_MV, 4)
                .save(provider);

        // MV SoC
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("processor_mv_soc").EUt(2400).duration(50)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(SYSTEM_ON_CHIP)
                .inputItems(wireFine, RedAlloy, 4)
                .inputItems(bolt, AnnealedCopper, 4)
                .outputItems(PROCESSOR_MV, 8)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // HV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("processor_assembly_hv").EUt(VA[MV]).duration(400)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(PROCESSOR_MV, 2)
                .inputItems(CustomTags.INDUCTORS, 4)
                .inputItems(CustomTags.CAPACITORS, 8)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(wireFine, RedAlloy, 8)
                .outputItems(PROCESSOR_ASSEMBLY_HV, 2)
                .solderMultiplier(2)
                .save(provider);

        // EV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("workstation_ev").EUt(VA[MV]).duration(400)
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(PROCESSOR_ASSEMBLY_HV, 2)
                .inputItems(CustomTags.DIODES, 4)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(wireFine, Electrum, 16)
                .inputItems(bolt, BlueAlloy, 16)
                .outputItems(WORKSTATION_EV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // IV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("mainframe_iv").EUt(VA[HV]).duration(800)
                .inputItems(frameGt, Aluminium, 2)
                .inputItems(WORKSTATION_EV, 2)
                .inputItems(CustomTags.INDUCTORS, 8)
                .inputItems(CustomTags.CAPACITORS, 16)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireGtSingle, AnnealedCopper, 16)
                .outputItems(MAINFRAME_IV)
                .solderMultiplier(4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("mainframe_iv_asmd").EUt(VA[HV]).duration(400)
                .inputItems(frameGt, Aluminium, 2)
                .inputItems(WORKSTATION_EV, 2)
                .inputItems(ADVANCED_SMD_INDUCTOR, 2)
                .inputItems(ADVANCED_SMD_CAPACITOR, 4)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireGtSingle, AnnealedCopper, 16)
                .outputItems(MAINFRAME_IV)
                .solderMultiplier(4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // T4: Nano ====================================================================================================

        // HV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_processor_hv").EUt(600).duration(200)
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT)
                .inputItems(SMD_RESISTOR, 8)
                .inputItems(SMD_CAPACITOR, 8)
                .inputItems(SMD_TRANSISTOR, 8)
                .inputItems(wireFine, Electrum, 8)
                .outputItems(NANO_PROCESSOR_HV, 4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_processor_hv_asmd").EUt(600).duration(100)
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT)
                .inputItems(ADVANCED_SMD_RESISTOR, 2)
                .inputItems(ADVANCED_SMD_CAPACITOR, 2)
                .inputItems(ADVANCED_SMD_TRANSISTOR, 2)
                .inputItems(wireFine, Electrum, 8)
                .outputItems(NANO_PROCESSOR_HV, 8)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // HV SoC
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_processor_hv_soc").EUt(9600).duration(50)
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(ADVANCED_SYSTEM_ON_CHIP)
                .inputItems(wireFine, Electrum, 4)
                .inputItems(bolt, Platinum, 4)
                .outputItems(NANO_PROCESSOR_HV, 8)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // EV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_processor_assembly_ev").EUt(600).duration(400)
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(NANO_PROCESSOR_HV, 2)
                .inputItems(SMD_INDUCTOR, 4)
                .inputItems(SMD_CAPACITOR, 8)
                .inputItems(RANDOM_ACCESS_MEMORY, 8)
                .inputItems(wireFine, Electrum, 16)
                .outputItems(NANO_PROCESSOR_ASSEMBLY_EV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_processor_assembly_ev_asmd").EUt(600).duration(200)
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(NANO_PROCESSOR_HV, 2)
                .inputItems(ADVANCED_SMD_INDUCTOR)
                .inputItems(ADVANCED_SMD_CAPACITOR, 2)
                .inputItems(RANDOM_ACCESS_MEMORY, 8)
                .inputItems(wireFine, Electrum, 16)
                .outputItems(NANO_PROCESSOR_ASSEMBLY_EV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // IV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_computer_iv").EUt(600).duration(400)
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(NANO_PROCESSOR_ASSEMBLY_EV, 2)
                .inputItems(SMD_DIODE, 8)
                .inputItems(NOR_MEMORY_CHIP, 4)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireFine, Electrum, 16)
                .outputItems(NANO_COMPUTER_IV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_computer_iv_asmd").EUt(600).duration(200)
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(NANO_PROCESSOR_ASSEMBLY_EV, 2)
                .inputItems(ADVANCED_SMD_DIODE, 2)
                .inputItems(NOR_MEMORY_CHIP, 4)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireFine, Electrum, 16)
                .outputItems(NANO_COMPUTER_IV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // LuV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_mainframe_luv").EUt(VA[EV]).duration(800)
                .inputItems(frameGt, Aluminium, 2)
                .inputItems(NANO_COMPUTER_IV, 2)
                .inputItems(SMD_INDUCTOR, 16)
                .inputItems(SMD_CAPACITOR, 32)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireGtSingle, AnnealedCopper, 32)
                .outputItems(NANO_MAINFRAME_LUV)
                .solderMultiplier(4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("nano_mainframe_luv_asmd").EUt(VA[EV]).duration(400)
                .inputItems(frameGt, Aluminium, 2)
                .inputItems(NANO_COMPUTER_IV, 2)
                .inputItems(ADVANCED_SMD_INDUCTOR, 4)
                .inputItems(ADVANCED_SMD_CAPACITOR, 8)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireGtSingle, AnnealedCopper, 32)
                .outputItems(NANO_MAINFRAME_LUV)
                .solderMultiplier(4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // T5: Quantum =================================================================================================

        // EV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_processor_ev").EUt(2400).duration(200)
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(QUBIT_CENTRAL_PROCESSING_UNIT)
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT)
                .inputItems(SMD_CAPACITOR, 12)
                .inputItems(SMD_TRANSISTOR, 12)
                .inputItems(wireFine, Platinum, 12)
                .outputItems(QUANTUM_PROCESSOR_EV, 4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_processor_ev_asmd").EUt(2400).duration(100)
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(QUBIT_CENTRAL_PROCESSING_UNIT)
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT)
                .inputItems(ADVANCED_SMD_CAPACITOR, 3)
                .inputItems(ADVANCED_SMD_TRANSISTOR, 3)
                .inputItems(wireFine, Platinum, 12)
                .outputItems(QUANTUM_PROCESSOR_EV, 4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // EV SoC
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_processor_ev_soc").EUt(38400).duration(50)
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(ADVANCED_SYSTEM_ON_CHIP)
                .inputItems(wireFine, Platinum, 12)
                .inputItems(bolt, NiobiumTitanium, 8)
                .outputItems(QUANTUM_PROCESSOR_EV, 8)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // IV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_assembly_iv").EUt(2400).duration(400)
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(QUANTUM_PROCESSOR_EV, 2)
                .inputItems(SMD_INDUCTOR, 8)
                .inputItems(SMD_CAPACITOR, 16)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(wireFine, Platinum, 16)
                .outputItems(QUANTUM_ASSEMBLY_IV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_assembly_iv_asmd").EUt(2400).duration(200)
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(QUANTUM_PROCESSOR_EV, 2)
                .inputItems(ADVANCED_SMD_INDUCTOR, 2)
                .inputItems(ADVANCED_SMD_CAPACITOR, 4)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(wireFine, Platinum, 16)
                .outputItems(QUANTUM_ASSEMBLY_IV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // LuV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_computer_luv").EUt(2400).duration(400)
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(QUANTUM_ASSEMBLY_IV, 2)
                .inputItems(SMD_DIODE, 8)
                .inputItems(NOR_MEMORY_CHIP, 4)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireFine, Platinum, 32)
                .outputItems(QUANTUM_COMPUTER_LUV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_computer_luv_asmd").EUt(2400).duration(200)
                .inputItems(EXTREME_CIRCUIT_BOARD)
                .inputItems(QUANTUM_ASSEMBLY_IV, 2)
                .inputItems(ADVANCED_SMD_DIODE, 2)
                .inputItems(NOR_MEMORY_CHIP, 4)
                .inputItems(RANDOM_ACCESS_MEMORY, 16)
                .inputItems(wireFine, Platinum, 32)
                .outputItems(QUANTUM_COMPUTER_LUV)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // ZPM
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_mainframe_zpm").EUt(VA[IV]).duration(800)
                .inputItems(frameGt, HSSG, 2)
                .inputItems(QUANTUM_COMPUTER_LUV, 2)
                .inputItems(SMD_INDUCTOR, 24)
                .inputItems(SMD_CAPACITOR, 48)
                .inputItems(RANDOM_ACCESS_MEMORY, 24)
                .inputItems(wireGtSingle, AnnealedCopper, 48)
                .solderMultiplier(4)
                .outputItems(QUANTUM_MAINFRAME_ZPM)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("quantum_mainframe_zpm_asmd").EUt(VA[IV]).duration(400)
                .inputItems(frameGt, HSSG, 2)
                .inputItems(QUANTUM_COMPUTER_LUV, 2)
                .inputItems(ADVANCED_SMD_INDUCTOR, 6)
                .inputItems(ADVANCED_SMD_CAPACITOR, 12)
                .inputItems(RANDOM_ACCESS_MEMORY, 24)
                .inputItems(wireGtSingle, AnnealedCopper, 48)
                .solderMultiplier(4)
                .outputItems(QUANTUM_MAINFRAME_ZPM)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // T6: Crystal =================================================================================================

        // IV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("crystal_processor_iv").EUt(9600).duration(200)
                .inputItems(ELITE_CIRCUIT_BOARD)
                .inputItems(CRYSTAL_CENTRAL_PROCESSING_UNIT)
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT, 2)
                .inputItems(ADVANCED_SMD_CAPACITOR, 6)
                .inputItems(ADVANCED_SMD_TRANSISTOR, 6)
                .inputItems(wireFine, NiobiumTitanium, 8)
                .outputItems(CRYSTAL_PROCESSOR_IV, 4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // IV SoC
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("crystal_processor_iv_soc").EUt(86000).duration(100)
                .inputItems(ELITE_CIRCUIT_BOARD)
                .inputItems(CRYSTAL_SYSTEM_ON_CHIP)
                .inputItems(wireFine, NiobiumTitanium, 8)
                .inputItems(bolt, YttriumBariumCuprate, 8)
                .outputItems(CRYSTAL_PROCESSOR_IV, 8)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // LuV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("crystal_assembly_luv").EUt(9600).duration(400)
                .inputItems(ELITE_CIRCUIT_BOARD)
                .inputItems(CRYSTAL_PROCESSOR_IV, 2)
                .inputItems(ADVANCED_SMD_INDUCTOR, 4)
                .inputItems(ADVANCED_SMD_CAPACITOR, 8)
                .inputItems(RANDOM_ACCESS_MEMORY, 24)
                .inputItems(wireFine, NiobiumTitanium, 16)
                .outputItems(CRYSTAL_ASSEMBLY_LUV, 2)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // ZPM
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("crystal_computer_zpm").EUt(9600).duration(400)
                .inputItems(ELITE_CIRCUIT_BOARD)
                .inputItems(CRYSTAL_ASSEMBLY_LUV, 2)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(NOR_MEMORY_CHIP, 32)
                .inputItems(NAND_MEMORY_CHIP, 64)
                .inputItems(wireFine, NiobiumTitanium, 32)
                .solderMultiplier(2)
                .outputItems(CRYSTAL_COMPUTER_ZPM, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // UV
        ASSEMBLY_LINE_RECIPES.recipeBuilder("crystal_mainframe_uv").EUt(VA[LuV]).duration(800)
                .inputItems(frameGt, HSSE, 2)
                .inputItems(CRYSTAL_COMPUTER_ZPM, 2)
                .inputItems(RANDOM_ACCESS_MEMORY, 32)
                .inputItems(HIGH_POWER_INTEGRATED_CIRCUIT, 2)
                .inputItems(wireGtSingle, NiobiumTitanium, 8)
                .inputItems(ADVANCED_SMD_INDUCTOR, 8)
                .inputItems(ADVANCED_SMD_CAPACITOR, 16)
                .inputItems(ADVANCED_SMD_DIODE, 8)
                .inputFluids(SolderingAlloy.getFluid(L * 10))
                .outputItems(CRYSTAL_MAINFRAME_UV)
                .save(provider);

        // T7: Wetware =================================================================================================

        // Neuro Processing Unit
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("neuro_processor").EUt(80000).duration(600)
                .inputItems(WETWARE_CIRCUIT_BOARD)
                .inputItems(STEM_CELLS, 16)
                .inputItems(pipeSmallFluid, Polybenzimidazole, 8)
                .inputItems(plate, Electrum, 8)
                .inputItems(foil, SiliconeRubber, 16)
                .inputItems(bolt, HSSE, 8)
                .inputFluids(SterileGrowthMedium.getFluid(250))
                .outputItems(NEURO_PROCESSOR, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // LuV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("wetware_processor_luv").EUt(38400).duration(200)
                .inputItems(NEURO_PROCESSOR)
                .inputItems(CRYSTAL_CENTRAL_PROCESSING_UNIT)
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT)
                .inputItems(ADVANCED_SMD_CAPACITOR, 8)
                .inputItems(ADVANCED_SMD_TRANSISTOR, 8)
                .inputItems(wireFine, YttriumBariumCuprate, 8)
                .outputItems(WETWARE_PROCESSOR_LUV, 4)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // SoC LuV
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("wetware_processor_luv_soc").EUt(150000).duration(100)
                .inputItems(NEURO_PROCESSOR)
                .inputItems(HIGHLY_ADVANCED_SOC)
                .inputItems(wireFine, YttriumBariumCuprate, 8)
                .inputItems(bolt, Naquadah, 8)
                .outputItems(WETWARE_PROCESSOR_LUV, 8)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // ZPM
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("wetware_processor_assembly_zpm").EUt(38400).duration(400)
                .inputItems(WETWARE_CIRCUIT_BOARD)
                .inputItems(WETWARE_PROCESSOR_LUV, 2)
                .inputItems(ADVANCED_SMD_INDUCTOR, 6)
                .inputItems(ADVANCED_SMD_CAPACITOR, 12)
                .inputItems(RANDOM_ACCESS_MEMORY, 24)
                .inputItems(wireFine, YttriumBariumCuprate, 16)
                .solderMultiplier(2)
                .outputItems(WETWARE_PROCESSOR_ASSEMBLY_ZPM, 2)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        // UV
        ASSEMBLY_LINE_RECIPES.recipeBuilder("wetware_super_computer_uv").EUt(38400).duration(400)
                .inputItems(WETWARE_CIRCUIT_BOARD)
                .inputItems(WETWARE_PROCESSOR_ASSEMBLY_ZPM, 2)
                .inputItems(ADVANCED_SMD_DIODE, 8)
                .inputItems(NOR_MEMORY_CHIP, 16)
                .inputItems(RANDOM_ACCESS_MEMORY, 32)
                .inputItems(wireFine, YttriumBariumCuprate, 24)
                .inputItems(foil, Polybenzimidazole, 32)
                .inputItems(plate, Europium, 4)
                .inputFluids(SolderingAlloy.getFluid(1152))
                .outputItems(WETWARE_SUPER_COMPUTER_UV, 2)
                .save(provider);

        // UHV
        ASSEMBLY_LINE_RECIPES.recipeBuilder("wetware_mainframe_uhv")
                .inputItems(frameGt, Tritanium, 2)
                .inputItems(WETWARE_SUPER_COMPUTER_UV, 2)
                .inputItems(ADVANCED_SMD_DIODE, 32)
                .inputItems(ADVANCED_SMD_CAPACITOR, 32)
                .inputItems(ADVANCED_SMD_TRANSISTOR, 32)
                .inputItems(ADVANCED_SMD_RESISTOR, 32)
                .inputItems(ADVANCED_SMD_INDUCTOR, 32)
                .inputItems(foil, Polybenzimidazole, 64)
                .inputItems(RANDOM_ACCESS_MEMORY, 32)
                .inputItems(wireGtDouble, EnrichedNaquadahTriniumEuropiumDuranide, 16)
                .inputItems(plate, Europium, 8)
                .inputFluids(SolderingAlloy.getFluid(L * 20))
                .inputFluids(Polybenzimidazole.getFluid(L * 8))
                .outputItems(WETWARE_MAINFRAME_UHV)
                .EUt(300000).duration(2000).save(provider);

        // Misc ========================================================================================================

        // Data Stick
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("data_stick")
                .inputItems(PLASTIC_CIRCUIT_BOARD)
                .inputItems(CENTRAL_PROCESSING_UNIT, 2)
                .inputItems(NAND_MEMORY_CHIP, 32)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(wireFine, RedAlloy, 16)
                .inputItems(plate, Polyethylene, 4)
                .outputItems(TOOL_DATA_STICK)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(400).EUt(90).save(provider);

        // Data Orb
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("data_orb")
                .inputItems(ADVANCED_CIRCUIT_BOARD)
                .inputItems(CustomTags.HV_CIRCUITS, 2)
                .inputItems(RANDOM_ACCESS_MEMORY, 4)
                .inputItems(NOR_MEMORY_CHIP, 32)
                .inputItems(NAND_MEMORY_CHIP, 64)
                .inputItems(wireFine, Platinum, 32)
                .outputItems(TOOL_DATA_ORB)
                .solderMultiplier(2)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(400).EUt(1200).save(provider);
    }


    private static void registerHighOutputWafers(Consumer<FinishedRecipe> provider) {
        CUTTER_RECIPES.recipeBuilder("cut_hasoc").duration(900).EUt(VA[IV]).inputItems(HIGHLY_ADVANCED_SOC_WAFER).outputItems(HIGHLY_ADVANCED_SOC, 24)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_asoc").duration(900).EUt(VA[EV]).inputItems(ADVANCED_SYSTEM_ON_CHIP_WAFER).outputItems(ADVANCED_SYSTEM_ON_CHIP, 24)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_soc").duration(900).EUt(VA[HV]).inputItems(SYSTEM_ON_CHIP_WAFER).outputItems(SYSTEM_ON_CHIP, 24)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_ssoc").duration(900).EUt(64).inputItems(SIMPLE_SYSTEM_ON_CHIP_WAFER).outputItems(SIMPLE_SYSTEM_ON_CHIP, 24).save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_ram").duration(900).EUt(96).inputItems(RANDOM_ACCESS_MEMORY_WAFER).outputItems(RANDOM_ACCESS_MEMORY, 64).outputItems(RANDOM_ACCESS_MEMORY, 64).save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_qbit_cpu").duration(900).EUt(VA[EV]).inputItems(QUBIT_CENTRAL_PROCESSING_UNIT_WAFER).outputItems(QUBIT_CENTRAL_PROCESSING_UNIT, 16)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_ulpic").duration(900).EUt(VA[MV]).inputItems(ULTRA_LOW_POWER_INTEGRATED_CIRCUIT_WAFER).outputItems(ULTRA_LOW_POWER_INTEGRATED_CIRCUIT, 24).save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_lpic").duration(900).EUt(VA[HV]).inputItems(LOW_POWER_INTEGRATED_CIRCUIT_WAFER).outputItems(LOW_POWER_INTEGRATED_CIRCUIT, 16)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_pic").duration(900).EUt(VA[EV]).inputItems(POWER_INTEGRATED_CIRCUIT_WAFER).outputItems(POWER_INTEGRATED_CIRCUIT, 16)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_hpic").duration(900).EUt(VA[IV]).inputItems(HIGH_POWER_INTEGRATED_CIRCUIT_WAFER).outputItems(HIGH_POWER_INTEGRATED_CIRCUIT, 8)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_uhpic").duration(900).EUt(VA[LuV]).inputItems(ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT_WAFER).outputItems(ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 8)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_nor").duration(900).EUt(192).inputItems(NOR_MEMORY_CHIP_WAFER).outputItems(NOR_MEMORY_CHIP, 64)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_nand").duration(900).EUt(192).inputItems(NAND_MEMORY_CHIP_WAFER).outputItems(NAND_MEMORY_CHIP, 64)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_cpu").duration(900).EUt(VA[MV]).inputItems(CENTRAL_PROCESSING_UNIT_WAFER).outputItems(CENTRAL_PROCESSING_UNIT, 32).save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_ilc").duration(900).EUt(64).inputItems(INTEGRATED_LOGIC_CIRCUIT_WAFER).outputItems(INTEGRATED_LOGIC_CIRCUIT, 32).save(provider);
        CUTTER_RECIPES.recipeBuilder("cut_nano_cpu").duration(900).EUt(VA[HV]).inputItems(NANO_CENTRAL_PROCESSING_UNIT_WAFER).outputItems(NANO_CENTRAL_PROCESSING_UNIT, 32)/*.cleanroom(CleanroomType.CLEANROOM)*/.save(provider);
    }

    private static void registerCrystalProcessors(Consumer<FinishedRecipe> provider) {
        BENDER_RECIPES.recipeBuilder("ao_raw_crystal_chip_emerald")
                .inputItems(new UnificationEntry(plate, Emerald))
                .outputItems(RAW_CRYSTAL_CHIP, 1)
                .EUt(VA[LuV])
                .duration(600)
                .circuitMeta(14)
                .save(provider);

        BENDER_RECIPES.recipeBuilder("ao_raw_crystal_chip_olivine")
                .inputItems(new UnificationEntry(plate, Olivine))
                .outputItems(RAW_CRYSTAL_CHIP, 1)
                .EUt(VA[LuV])
                .duration(600)
                .circuitMeta(14)
                .save(provider);

        BLAST_RECIPES.recipeBuilder("engraved_crystal_chip_from_emerald")
                .inputItems(plate, Emerald)
                .inputItems(RAW_CRYSTAL_CHIP)
                .outputItems(ENGRAVED_CRYSTAL_CHIP.asStack(8))
                .blastFurnaceTemp(5000)
                .duration(900).EUt(VA[HV]).save(provider);

        BLAST_RECIPES.recipeBuilder("engraved_crystal_chip_from_olivine")
                .inputItems(plate, Olivine)
                .inputItems(RAW_CRYSTAL_CHIP)
                .outputItems(ENGRAVED_CRYSTAL_CHIP.asStack(8))
                .blastFurnaceTemp(5000)
                .duration(900).EUt(VA[HV]).save(provider);
    }
}
