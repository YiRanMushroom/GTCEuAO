package com.yiranmushroom.gtceuao.recipes.machine;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class EasyChemistryRecipes {
    public static void register(Consumer<FinishedRecipe> provider) {
        if (AOConfigHolder.INSTANCE.recipes.easierPolymerRecipes)
            registerEasierPolymers(provider);

        if (AOConfigHolder.INSTANCE.recipes.easierWroughtIronAndSteel)
            registerEasierWroughtIronAndSteel(provider);

    }

    private static void registerEasierWroughtIronAndSteel(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addSmeltingRecipe(provider, "wrought_iron_ingot", ChemicalHelper.getTag(ingot, Iron), ChemicalHelper.get(ingot, WroughtIron));
        VanillaRecipeHelper.addBlastingRecipe(provider, "wrought_iron_ingot_blast", ChemicalHelper.getTag(ingot, Iron), ChemicalHelper.get(ingot, WroughtIron), 4F);

        VanillaRecipeHelper.addShapelessRecipe(provider, "easy_rubber_plate", ChemicalHelper.get(plate, Rubber), 'h', ChemicalHelper.get(ingot, Rubber));

        VanillaRecipeHelper.addSmeltingRecipe(provider, "rubber_from_resin", STICKY_RESIN.asStack(1), ChemicalHelper.get(ingot, Rubber));

        BLAST_RECIPES.recipeBuilder("steel_from_iron_custom").duration(1000).EUt(VA[LV]).inputItems(ingot, Iron).outputItems(ingot, Steel).outputItems(dustTiny, Ash).blastFurnaceTemp(1000).circuitMeta(14).save(provider);
        BLAST_RECIPES.recipeBuilder("steel_from_wrought_iron_custom").duration(600).EUt(VA[LV]).inputItems(ingot, WroughtIron).outputItems(ingot, Steel).outputItems(dustTiny, Ash).blastFurnaceTemp(1000).circuitMeta(14).save(provider);

        ARC_FURNACE_RECIPES.recipeBuilder("steel_from_wrought_iron_arc_furnace").duration(100).EUt(VA[MV]).inputItems(ingot, WroughtIron).outputItems(ingot, Steel).save(provider);

        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coal_gem").inputItems(ingot, Iron).inputItems(gem, Coal).outputItems(ingot, Steel).duration(1800).EUt(VA[ULV]).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coal_dust").inputItems(ingot, Iron).inputItems(dust, Coal).outputItems(ingot, Steel).duration(1800).EUt(VA[ULV]).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_charcoal_gem").inputItems(ingot, Iron).inputItems(gem, Charcoal).outputItems(ingot,Steel).duration(1800).EUt(VA[ULV]).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_charcoal_dust").inputItems(ingot, Iron).inputItems(dust, Charcoal).outputItems(ingot, Steel).duration(1800).EUt(VA[ULV]).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coke_gem").inputItems(ingot, Iron, 2).inputItems(gem, Coke).outputItems(ingot, Steel, 2).duration(1500).EUt(VA[ULV]).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coke_dust").inputItems(ingot, Iron, 2).inputItems(dust, Coke).outputItems(ingot, Steel, 2).duration(1500).EUt(VA[ULV]).save(provider);

        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coal_block").inputItems(block, Iron).inputItems(block, Coal).outputItems(block, Steel).EUt(VA[ULV]).duration(16200).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_charcoal_block").inputItems(block, Iron).inputItems(block, Charcoal).outputItems(block, Steel).EUt(VA[ULV]).duration(16200).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coke_block").inputItems(block, Iron, 2).inputItems(block, Coke).outputItems(block, Steel, 2).duration(13500).EUt(VA[ULV]).save(provider);

        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coal_gem_wrought").inputItems(ingot, WroughtIron).inputItems(gem, Coal).outputItems(ingot, Steel).EUt(VA[ULV]).duration(800).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coal_dust_wrought").inputItems(ingot, WroughtIron).inputItems(dust, Coal).outputItems(ingot, Steel).EUt(VA[ULV]).duration(800).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_charcoal_gem_wrought").inputItems(ingot, WroughtIron).inputItems(gem, Charcoal).outputItems(ingot, Steel).EUt(VA[ULV]).duration(800).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_charcoal_dust_wrought").inputItems(ingot, WroughtIron).inputItems(dust, Charcoal).outputItems(ingot, Steel).EUt(VA[ULV]).duration(800).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coke_gem_wrought").inputItems(ingot, WroughtIron, 2).inputItems(gem, Coke).outputItems(ingot, Steel, 2).duration(600).EUt(VA[ULV]).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coke_dust_wrought").inputItems(ingot, WroughtIron, 2).inputItems(dust, Coke).outputItems(ingot, Steel, 2).duration(600).EUt(VA[ULV]).save(provider);

        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coal_block_wrought").inputItems(block, WroughtIron).inputItems(block, Coal).outputItems(block, Steel).outputItems(dust, DarkAsh, 2).duration(7200).EUt(VA[ULV]).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_charcoal_block_wrought").inputItems(block, WroughtIron).inputItems(block, Charcoal).outputItems(block, Steel).EUt(VA[ULV]).duration(7200).save(provider);
        ALLOY_SMELTER_RECIPES.recipeBuilder("alloy_steel_from_coke_block_wrought").inputItems(block, WroughtIron, 2).inputItems(block, Coke).outputItems(block, Steel, 2).EUt(VA[ULV]).duration(5400).save(provider);
    }

    private static void registerEasierPolymers(Consumer<FinishedRecipe> provider) {
        CHEMICAL_RECIPES.recipeBuilder("ethanol_to_polyethylene")
            .inputFluids(Ethanol.getFluid(1000))
            .outputFluids(Polyethylene.getFluid(1440))
            .duration(1200)
            .EUt(VA[LV])
            .circuitMeta(14)
            .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("polyethylene_to_polyvinyl_chloride")
            .inputFluids(Polyethylene.getFluid(1440))
            .inputFluids(Chlorine.getFluid(1000))
            .outputFluids(PolyvinylChloride.getFluid(2160))
            .duration(1200)
            .EUt(VA[LV])
            .circuitMeta(14)
            .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("polyethylene_to_polytetrafluoroethylene")
            .inputFluids(Polyethylene.getFluid(1440))
            .inputFluids(Fluorine.getFluid(1000))
            .outputFluids(Polytetrafluoroethylene.getFluid(2160))
            .duration(1200)
            .EUt(VA[LV])
            .circuitMeta(14)
            .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("bio_chaff_to_ethanol")
            .inputItems(BIO_CHAFF)
            .inputFluids(Water.getFluid(500))
            .outputFluids(Ethanol.getFluid(500))
            .duration(160)
            .EUt(VA[LV])
            .circuitMeta(14)
            .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("cover_infinite_water")
            .inputItems(ELECTRIC_PUMP_LV)
            .inputItems(new ItemStack(Items.CAULDRON))
            .inputItems(CustomTags.LV_CIRCUITS)
            .outputItems(COVER_INFINITE_WATER, 4)
            .EUt(VA[LV]).duration(100)
            .save(provider);

        CHEMICAL_RECIPES.recipeBuilder("pure_rubber")
            .inputItems(dust, RawRubber, 10)
            .outputFluids(Rubber.getFluid(1440))
            .EUt(VA[ULV])
            .duration(1200)
            .circuitMeta(14)
            .save(provider);

    }
}

