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
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityMachineRecipeLoader;
import com.gregtechceu.gtceu.integration.ae2.machine.MEBusPartMachine;
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

public class EasyOreProcessing {
    public static void register(Consumer<FinishedRecipe> provider) {
        if (!AOConfigHolder.INSTANCE.recipes.easierOreProcessing)
            return;

        ELECTROLYZER_RECIPES.recipeBuilder("rutile_to_titanium")
                .inputItems(dust, Rutile)
                .outputFluids(Oxygen.getFluid(2000))
                .outputItems(dust, Titanium)
                .EUt(VA[EV]).duration(200).save(provider);

        ELECTROLYZER_RECIPES.recipeBuilder("tungstate_to_tungsten")
                .inputItems(dust, Tungstate, 7)
                .outputItems(dust, Lithium, 2)
                .outputItems(dust, Tungsten, 1)
                .outputFluids(Oxygen.getFluid(4000))
                .EUt(VA[EV]).duration(200).save(provider);

        ELECTROLYZER_RECIPES.recipeBuilder("scheelite_to_tungsten")
                .inputItems(dust, Scheelite, 6)
                .outputItems(dust, Calcium, 1)
                .outputItems(dust, Tungsten, 1)
                .outputFluids(Oxygen.getFluid(4000))
                .EUt(VA[EV]).duration(200).save(provider);

        ELECTROLYZER_RECIPES.recipeBuilder("uraninite_to_uranium")
                .inputItems(dust, Uraninite, 10)
                .outputItems(dust, Uranium238, 9)
                .outputItems(dust, Uranium235, 1)
                .outputFluids(Oxygen.getFluid(20000))
                .EUt(VA[EV]).duration(1200).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("indium_concentrate_separation")
                .circuitMeta(1)
                .notConsumable(dust, Aluminium)
                .inputFluids(IndiumConcentrate.getFluid(1000))
                .outputItems(dust, Indium)
                .outputFluids(LeadZincSolution.getFluid(1000))
                .duration(50).EUt(600).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("indium_concentrate_separation_4x")
                .circuitMeta(4)
                .notConsumable(dust, Aluminium)
                .inputFluids(IndiumConcentrate.getFluid(4000))
                .outputItems(dust, Indium, 4)
                .outputFluids(LeadZincSolution.getFluid(4000))
                .duration(200).EUt(600).save(provider);
    }
}
