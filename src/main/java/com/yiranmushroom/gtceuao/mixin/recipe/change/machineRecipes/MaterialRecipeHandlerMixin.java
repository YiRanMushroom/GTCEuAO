package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.MaterialRecipeHandler;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.gtceuao;
import dev.emi.emi.config.HelpLevel;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(MaterialRecipeHandler.class)
public class MaterialRecipeHandlerMixin {

    /**
     * @author YiranMushroom
     * @reason I don't why but injection doesn't work here, so I have to overwrite this method.
     */
    @Overwrite(remap = false)
    private static void processEBFRecipe(Material material, BlastProperty property, ItemStack output, Consumer<FinishedRecipe> provider) {

        if (!AOConfigHolder.INSTANCE.recipes.ingotNeedEBF) {
            if (ingot.doGenerateItem(material)) {
                VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + material.getName() + "_to_ingot", ChemicalHelper.get(dust, material), ChemicalHelper.get(ingot, material));
                VanillaRecipeHelper.addBlastingRecipe(provider, "blast_smelt_" + material.getName() + "_to_ingot", ChemicalHelper.getTag(dust, material), ChemicalHelper.get(ingot, material), 0.5F);
            } else {
                VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + material.getName(), ChemicalHelper.get(dust, material), output);
                VanillaRecipeHelper.addBlastingRecipe(provider, "blast_smelt_" + material.getName(), ChemicalHelper.getTag(dust, material), output, 0.5F);
            }
        }

        int blastTemp = property.getBlastTemperature();
        BlastProperty.GasTier gasTier = property.getGasTier();
        int duration = property.getDurationOverride();
        if (duration <= 0) {
            duration = Math.max(1, (int) (material.getMass() * blastTemp / 50L));
        }
        int EUt = property.getEUtOverride();
        if (EUt <= 0) EUt = VA[MV];

        GTRecipeBuilder blastBuilder = BLAST_RECIPES.recipeBuilder("blast_" + material.getName())
            .inputItems(dust, material)
            .outputItems(output)
            .blastFurnaceTemp(blastTemp)
            .EUt(EUt);

        if (gasTier != null) {
            FluidIngredient gas = CraftingComponent.EBF_GASES.get(gasTier).copy();

            blastBuilder.copy("blast_" + material.getName())
                .circuitMeta(1)
                .duration(duration)
                .save(provider);

            blastBuilder.copy("blast_" + material.getName() + "_gas")
                .circuitMeta(2)
                .inputFluids(gas)
                .duration((int) (duration * 0.67))
                .save(provider);
        } else {
            blastBuilder.duration(duration);
            if (material == Silicon) {
                blastBuilder.circuitMeta(1);
            }
            blastBuilder.save(provider);
        }

        // Add Vacuum Freezer recipe if required.
        if (ingotHot.doGenerateItem(material)) {
            if (material.getBlastTemperature() < 5000 || (!AOConfigHolder.INSTANCE.recipes.hotIngotNeedLiquidHelium)) {
                VACUUM_RECIPES.recipeBuilder("cool_hot_" + material.getName() + "_ingot")
                    .inputItems(ingotHot, material)
                    .outputItems(ingot, material)
                    .duration((int) material.getMass() * 3)
                    .save(provider);
            } else {
                VACUUM_RECIPES.recipeBuilder("cool_hot_" + material.getName() + "_ingot")
                    .inputItems(ingotHot, material)
                    .outputItems(ingot, material)
                    .outputFluids(Helium.getFluid(FluidStorageKeys.LIQUID, 250))
                    .duration((int) material.getMass() * 3)
                    .save(provider);
            }
        }
    }
}
