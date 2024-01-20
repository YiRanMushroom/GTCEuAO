package com.yiranmushroom.gtceuao.recipes.machine;

import appeng.api.ids.AEItemIds;
import appeng.api.util.AEColor;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityMachineRecipeLoader;
import com.gregtechceu.gtceu.integration.ae2.GTAEMachines;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.Items;


import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;


import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class AERecipes {
    public static void register(Consumer<FinishedRecipe> provider) {
        if (AOConfigHolder.INSTANCE.recipes.AE2RecipeSupport && GTCEu.isAE2Loaded())
            registerAE2(provider);

    }

    private static void registerAE2(Consumer<FinishedRecipe> provider) {
        // Use Tag<Item> in the future

        ItemStack meInterface = AEParts.INTERFACE.stack(1);
//        ItemStack accelerationCard = AEItems.SPEED_CARD.stack(2);

        ASSEMBLER_RECIPES.recipeBuilder("me_export_hatch")
                .inputItems(FLUID_EXPORT_HATCH[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.FLUID_EXPORT_HATCH.asStack())
                .duration(300).EUt(VA[MV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("me_import_hatch")
                .inputItems(FLUID_IMPORT_HATCH[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.FLUID_IMPORT_HATCH.asStack())
                .duration(300).EUt(VA[MV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("me_export_bus")
                .inputItems(ITEM_EXPORT_BUS[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.ITEM_EXPORT_BUS.asStack())
                .duration(300).EUt(VA[MV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("me_import_bus")
                .inputItems(ITEM_IMPORT_BUS[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.ITEM_IMPORT_BUS.asStack())
                .duration(300).EUt(VA[MV]).save(provider);

        MACERATOR_RECIPES.recipeBuilder("quartz_sand_to_silicon")
                .inputItems(new UnificationEntry(dust, QuartzSand))
                .outputItems(AEItems.SILICON.stack())
                .duration(100).EUt(VA[ULV]).save(provider);

        // Circuits

        BENDER_RECIPES.recipeBuilder("silicon_circuit")
                .inputItems(AEItems.SILICON.stack())
                .outputItems(AEItems.SILICON_PRINT.stack(4))
                .circuitMeta(14)
                .duration(160).EUt(VA[LV]).save(provider);

        BENDER_RECIPES.recipeBuilder("gold_circuit")
                .inputItems(new UnificationEntry(ingot, Gold))
                .outputItems(AEItems.LOGIC_PROCESSOR_PRINT.stack(4))
                .circuitMeta(14)
                .duration(160).EUt(VA[LV]).save(provider);

        BENDER_RECIPES.recipeBuilder("diamond_circuit")
                .inputItems(new UnificationEntry(gem, Diamond))
                .outputItems(AEItems.ENGINEERING_PROCESSOR_PRINT.stack(4))
                .circuitMeta(14)
                .duration(160).EUt(VA[LV]).save(provider);

        /*BENDER_RECIPES.recipeBuilder("certus_circuit")
                .inputItems(AEItems.CERTUS_QUARTZ_CRYSTAL.stack())
                .outputItems(AEItems.CALCULATION_PROCESSOR_PRINT.stack(4))
                .circuitMeta(14)
                .duration(160).EUt(VA[LV]).save(provider);*/

        // From GregTech
        BENDER_RECIPES.recipeBuilder("silicon_circuit_g")
                .inputItems(new UnificationEntry(ingot, Silicon))
                .outputItems(AEItems.SILICON_PRINT.stack(4))
                .circuitMeta(14)
                .duration(160).EUt(VA[LV]).save(provider);

        BENDER_RECIPES.recipeBuilder("certus_circuit_g")
                .inputItems(new UnificationEntry(gem, CertusQuartz))
                .outputItems(AEItems.CALCULATION_PROCESSOR_PRINT.stack(4))
                .circuitMeta(14)
                .duration(160).EUt(VA[LV]).save(provider);

        // Processors

        FORMING_PRESS_RECIPES.recipeBuilder("logic_processor")
                .inputItems(AEItems.LOGIC_PROCESSOR_PRINT.stack())
                .inputItems(new UnificationEntry(dust, Redstone))
                .inputItems(AEItems.SILICON_PRINT.stack())
                .outputItems(AEItems.LOGIC_PROCESSOR.stack(4))
                .duration(320).EUt(VA[LV]).save(provider);

        FORMING_PRESS_RECIPES.recipeBuilder("engineering_processor")
                .inputItems(AEItems.ENGINEERING_PROCESSOR_PRINT.stack())
                .inputItems(new UnificationEntry(dust, Redstone))
                .inputItems(AEItems.SILICON_PRINT.stack())
                .outputItems(AEItems.ENGINEERING_PROCESSOR.stack(4))
                .duration(320).EUt(VA[LV]).save(provider);

        FORMING_PRESS_RECIPES.recipeBuilder("calculation_processor")
                .inputItems(AEItems.CALCULATION_PROCESSOR_PRINT.stack())
                .inputItems(new UnificationEntry(dust, Redstone))
                .inputItems(AEItems.SILICON_PRINT.stack())
                .outputItems(AEItems.CALCULATION_PROCESSOR.stack(4))
                .duration(320).EUt(VA[LV]).save(provider);

        // For Fluix Crystal
        POLARIZER_RECIPES.recipeBuilder("charged_certus_quartz_crystal")
                .inputItems(new UnificationEntry(gem, CertusQuartz))
                .outputItems(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED.stack())
                .duration(40).EUt(VA[LV]).save(provider);

        MIXER_RECIPES.recipeBuilder("fluix_crystal")
                .inputItems(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED.stack())
                .inputItems(new UnificationEntry(dust, Redstone))
                .inputItems(new UnificationEntry(gem, NetherQuartz))
                .outputItems(AEItems.FLUIX_CRYSTAL.stack(16))
                .duration(40).EUt(VA[LV]).save(provider);

        MACERATOR_RECIPES.recipeBuilder("fluix_dust")
                .inputItems(AEItems.FLUIX_CRYSTAL.stack())
                .outputItems(AEItems.FLUIX_DUST.stack())
                .duration(40).EUt(VA[LV]).save(provider);

        ALLOY_SMELTER_RECIPES.recipeBuilder("quartz_glass_nether")
                .inputItems(new UnificationEntry(dust, NetherQuartz))
                .inputItems(new UnificationEntry(block, Glass))
                .outputItems(AEBlocks.QUARTZ_GLASS.stack(8))
                .duration(200).EUt(VA[LV]).save(provider);

        ALLOY_SMELTER_RECIPES.recipeBuilder("quartz_glass_certus")
                .inputItems(new UnificationEntry(dust, CertusQuartz))
                .inputItems(new UnificationEntry(block, Glass))
                .outputItems(AEBlocks.QUARTZ_GLASS.stack(8))
                .duration(200).EUt(VA[LV]).save(provider);

        LATHE_RECIPES.recipeBuilder("quartz_fiber_nether")
                .inputItems(new UnificationEntry(gem, NetherQuartz))
                .outputItems(AEParts.QUARTZ_FIBER.stack(8))
                .duration(80).EUt(VA[LV]).save(provider);

        LATHE_RECIPES.recipeBuilder("quartz_fiber_certus")
                .inputItems(new UnificationEntry(gem, CertusQuartz))
                .outputItems(AEParts.QUARTZ_FIBER.stack(8))
                .duration(80).EUt(VA[LV]).save(provider);

        ALLOY_SMELTER_RECIPES.recipeBuilder("fluix_cable")
                .inputItems(AEItems.FLUIX_CRYSTAL.stack(2))
                .inputItems(AEParts.QUARTZ_FIBER.stack())
                .outputItems(AEParts.GLASS_CABLE.stack(AEColor.TRANSPARENT, 8))
                .duration(200).EUt(VA[LV]).save(provider);

        // Creative Energy Cell
        ELECTROLYZER_RECIPES.recipeBuilder("creative_energy_cell")
                .inputItems(AEBlocks.FLUIX_BLOCK.stack(4))
                .outputItems(AEBlocks.CREATIVE_ENERGY_CELL.stack())
                .duration(24000).EUt(VA[ULV]).save(provider);

        // Components
        Material[] materialTierList = {Iron, WroughtIron, Steel, Aluminium, StainlessSteel, Titanium, TungstenSteel};
        for (int i = 0; i < materialTierList.length; i++) {
            int initial_2 = 2 << i;
            int initial_1 = 1 << i;
            int initial_4 = 4 << i;

            // Pattern
            if (initial_2 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("pattern_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]), 2)
                        .inputItems(AEBlocks.QUARTZ_GLASS.stack(2))
                        .inputItems(new UnificationEntry(gem, CertusQuartz))
                        .inputItems(new UnificationEntry(dust, Glowstone))
                        .outputItems(AEItems.BLANK_PATTERN.stack(initial_2))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Basic Card
            if (initial_2 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("basic_card_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]), 2)
                        .inputItems(new UnificationEntry(plate, Gold))
                        .inputItems(AEItems.CALCULATION_PROCESSOR.stack())
                        .inputItems(new UnificationEntry(dust, Redstone))
                        .outputItems(AEItems.BASIC_CARD.stack(initial_2))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Advanced Card
            if (initial_2 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("advanced_card_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]), 2)
                        .inputItems(new UnificationEntry(gem, Diamond))
                        .inputItems(AEItems.CALCULATION_PROCESSOR.stack())
                        .inputItems(new UnificationEntry(dust, Redstone))
                        .outputItems(AEItems.ADVANCED_CARD.stack(initial_2))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Cable Anchor
            if (initial_4 <= 64)
                CUTTER_RECIPES.recipeBuilder("cable_anchor_with_tier_"+ String.valueOf(i))
                        .inputItems(new UnificationEntry(ingot, materialTierList[i]))
                        .outputItems(AEParts.CABLE_ANCHOR.stack(initial_4))
                        .EUt(VA[LV])
                        .duration(20).save(provider);

            // Import Bus
            if (initial_1 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("import_bus_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]))
                        .inputItems(Items.STICKY_PISTON)
                        .inputItems(AEItems.ANNIHILATION_CORE.stack())
                        .outputItems(AEParts.IMPORT_BUS.stack(initial_1))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Export Bus
            if (initial_1 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("export_bus_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]))
                        .inputItems(Items.PISTON)
                        .inputItems(AEItems.FORMATION_CORE.stack())
                        .outputItems(AEParts.EXPORT_BUS.stack(initial_1))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Crafting Unit
            if (initial_1 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("crafting_unit_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]), 3)
                        .inputItems(AEParts.GLASS_CABLE.stack(AEColor.TRANSPARENT, 2))
                        .inputItems(AEItems.CALCULATION_PROCESSOR.stack())
                        .inputItems(AEItems.LOGIC_PROCESSOR.stack())
                        .outputItems(AEBlocks.CRAFTING_UNIT.stack(initial_1))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Interface
            if (initial_1 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("interface_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]), 3)
                        .inputItems(AEItems.FORMATION_CORE.stack())
                        .inputItems(AEItems.ANNIHILATION_CORE.stack())
                        .inputItems(Items.GLASS)
                        .outputItems(AEBlocks.INTERFACE.stack(initial_1))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Pattern Provider
            if (initial_1 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("pattern_provider_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]), 3)
                        .inputItems(AEItems.FORMATION_CORE.stack())
                        .inputItems(AEItems.ANNIHILATION_CORE.stack())
                        .inputItems(Items.CRAFTING_TABLE)
                        .outputItems(AEBlocks.PATTERN_PROVIDER.stack(initial_1))
                        .circuitMeta(14)
                        .duration(200).EUt(VA[MV]).save(provider);

            // Molecular Assembler use meta 1
            if (initial_1 <= 64)
                ASSEMBLER_RECIPES.recipeBuilder("molecular_assembler_with_tier_" + String.valueOf(i))
                        .inputItems(new UnificationEntry(plate, materialTierList[i]), 3)
                        .inputItems(AEItems.FORMATION_CORE.stack())
                        .inputItems(AEItems.ANNIHILATION_CORE.stack())
                        .inputItems(Items.CRAFTING_TABLE)
                        .inputItems(AEBlocks.QUARTZ_GLASS.stack())
                        .outputItems(AEBlocks.MOLECULAR_ASSEMBLER.stack(initial_1))
                        .circuitMeta(1)
                        .duration(200).EUt(VA[MV]).save(provider);
        }
    }
}

/*
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;

@Mixin(MetaTileEntityMachineRecipeLoader.class)
public class MetaTileEntityMachineRecipeLoaderMixin {

}
*/

