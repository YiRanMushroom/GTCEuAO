package com.yiranmushroom.gtceuao.recipes.machine;

import com.google.common.collect.ImmutableMap;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTItems.HIGHLY_ADVANCED_SOC_WAFER;
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

        BLAST_RECIPES.recipeBuilder("steel_from_iron").duration(1000).EUt(VA[LV]).inputItems(ingot, Iron).inputFluids(Oxygen.getFluid(200)).outputItems(ingot, Steel).outputItems(dustTiny, Ash).blastFurnaceTemp(1000).save(provider);
        BLAST_RECIPES.recipeBuilder("steel_from_wrought_iron").duration(600).EUt(VA[LV]).inputItems(ingot, WroughtIron).inputFluids(Oxygen.getFluid(200)).outputItems(ingot, Steel).outputItems(dustTiny, Ash).blastFurnaceTemp(1000).save(provider);

        ARC_FURNACE_RECIPES.recipeBuilder("steel_from_wrought_iron_arc_furnace").duration(100).EUt(VA[MV]).inputItems(ingot, WroughtIron).outputItems(ingot, Steel).save(provider);
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

