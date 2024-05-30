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
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.Items;


import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;


import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class ExNihiloRecipe {
    public static void register(Consumer<FinishedRecipe> provider) {
        if (!AOConfigHolder.INSTANCE.recipes.exNihiloLikeRecipesAndMaterials)
            return;

        CHEMICAL_RECIPES.recipeBuilder("sand_to_clay")
            .inputItems(Items.SAND)
            .inputFluids(Water.getFluid(100))
            .circuitMeta(1)
            .outputItems(block, Clay)
            .EUt(VA[LV]).duration(100).save(provider);

        CHEMICAL_RECIPES.recipeBuilder("sand_to_clay_dust")
            .inputItems(Items.SAND)
            .inputFluids(Water.getFluid(100))
            .circuitMeta(2)
            .outputItems(dust, Clay, 4)
            .EUt(VA[LV]).duration(100).save(provider);

        FLUID_HEATER_RECIPES.recipeBuilder("water_to_salt_water")
            .inputFluids(Water.getFluid(4000))
            .outputFluids(SaltWater.getFluid(1000))
            .circuitMeta(14)
            .EUt(VA[LV]).duration(100).save(provider);

        MACERATOR_RECIPES.recipeBuilder("macerate_granite")
            .inputItems(Blocks.GRANITE.asItem())
            .outputItems(dust, Granite)
            .chancedOutput(dust, Stone, 100, 50)
            .chancedOutput(dust, Uranium238, 10, 5)
            .duration(150).EUt(2)
            .save(provider);
    }
}
