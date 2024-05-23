package com.yiranmushroom.gtceuao.recipes.machine;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.core.Materials;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifier;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class OPNaquadahLineRecipes {
    public static void register(Consumer<FinishedRecipe> provider) {
        if (!AOConfigHolder.INSTANCE.recipes.OPNaquadahLine)
            return;

        LARGE_CHEMICAL_RECIPES.recipeBuilder("naquadah_separation").EUt(VA[LuV]).duration(600)
            .inputFluids(FluoroantimonicAcid.getFluid(1000))
            .inputItems(dust, Naquadah, 6)
            .outputFluids(ImpureEnrichedNaquadahSolution.getFluid(2000))
            .outputFluids(ImpureNaquadriaSolution.getFluid(2000))
            .outputItems(dust, TitaniumTrifluoride, 4)
            .circuitMeta(1)
            .save(provider);

        LARGE_CHEMICAL_RECIPES.recipeBuilder("naquadah_separation_2").EUt(VA[HV]).duration(20 * 60 * 30)
            .inputFluids(FluoroantimonicAcid.getFluid(2000))
            .inputItems(dust, Naquadah, 1)
            .inputItems(dust, Naquadria, 1)
            .inputItems(dust, NaquadahEnriched, 1)
            .circuitMeta(14)
            .outputFluids(ImpureEnrichedNaquadahSolution.getFluid(4000))
            .outputFluids(ImpureNaquadriaSolution.getFluid(4000))
            .outputItems(dust, TitaniumTrifluoride, 8)
            .circuitMeta(2)
            .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder("impure_enriched_naquadah_solution_separation").EUt(VA[EV]).duration(400)
            .inputFluids(ImpureEnrichedNaquadahSolution.getFluid(2000))
            .outputItems(dust, TriniumSulfide, 2)
            .outputItems(dust, AntimonyTrifluoride, 2)
            .outputItems(dustSmall, Tungsten)
            .outputFluids(EnrichedNaquadahSolution.getFluid(1000))
            .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder("impure_naquadria_solution_separation").EUt(VA[EV]).duration(400)
            .inputFluids(ImpureNaquadriaSolution.getFluid(2000))
            .outputItems(dust, Indium)
            .outputItems(dust, Sulfur)
            .outputItems(dust, AntimonyTrifluoride, 2)
            .outputItems(dustSmall, Gallium)
            .outputFluids(NaquadriaSolution.getFluid(1000))
            .save(provider);

        DISTILLATION_RECIPES.recipeBuilder("enriched_naquadah_waste_separation").EUt(VA[HV]).duration(300)
            .inputFluids(EnrichedNaquadahWaste.getFluid(2000))
            .chancedOutput(dust, Naquadah, 4, 5000, 2500)
            .outputFluids(SulfuricAcid.getFluid(500))
            .outputFluids(EnrichedNaquadahSolution.getFluid(250))
            .outputFluids(NaquadriaSolution.getFluid(100))
            .save(provider);

        DISTILLATION_RECIPES.recipeBuilder("naquadria_waste_separation").EUt(VA[HV]).duration(300)
            .inputFluids(NaquadriaWaste.getFluid(2000))
            .chancedOutput(dust, Chromium, 5000, 2500)
            .outputFluids(SulfuricAcid.getFluid(500))
            .outputFluids(NaquadriaSolution.getFluid(250))
            .outputFluids(EnrichedNaquadahSolution.getFluid(100))
            .save(provider);

        BLAST_RECIPES.recipeBuilder("trinium_sulfide_separation").duration(750).EUt(VA[IV]).blastFurnaceTemp(3000)
            .inputItems(dust, TriniumSulfide, 2)
            .outputItems(ingot, Trinium)
            .outputItems(nugget, Iridium, 3)
            .outputItems(nugget, Osmium, 3)
            .outputFluids(SulfuricAcid.getFluid(1000))
            .save(provider);

        BLAST_RECIPES.recipeBuilder("naquadria_sulfate_separation").EUt(VA[IV]).duration(1000).blastFurnaceTemp(3000)
            .inputItems(dust, NaquadriaSulfate, 6)
            .inputFluids(Hydrogen.getFluid(2000))
            .outputItems(ingot, Naquadria)
            .outputItems(nugget, Neutronium)
            .outputItems(nugget, Samarium, 3)
            .outputFluids(SulfuricAcid.getFluid(1000))
            .save(provider);

        BLAST_RECIPES.recipeBuilder("enriched_naquadah_sulfate_separation").EUt(VA[IV]).duration(500).blastFurnaceTemp(3000)
            .inputItems(dust, EnrichedNaquadahSulfate, 6)
            .inputFluids(Hydrogen.getFluid(2000))
            .outputItems(ingot, NaquadahEnriched)
            .outputItems(nugget, Darmstadtium)
            .outputItems(nugget, Platinum, 3)
            .outputFluids(SulfuricAcid.getFluid(1000))
            .save(provider);

        BLAST_RECIPES.recipeBuilder("titanium_trifluoride_separation").EUt(VA[IV]).duration(120).blastFurnaceTemp(3000)
            .inputItems(dust, TitaniumTrifluoride, 4)
            .inputFluids(Hydrogen.getFluid(3000))
            .outputItems(ingot, Titanium)
            .outputItems(nugget, Niobium, 2)
            .outputItems(nugget, Rhodium, 2)
            .outputFluids(HydrofluoricAcid.getFluid(3000))
            .save(provider);


    }
}
