package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.*;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.MaterialRecipeHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
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


import java.util.Locale;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(MaterialRecipeHandler.class)
public class MaterialRecipeHandlerMixin {
    /**
     * @author YiranMushroom
     * @reason buff autoclave recipe
     */
    @Overwrite(remap = false)
    public static void processDust(TagPrefix dustPrefix, Material mat, DustProperty property, Consumer<FinishedRecipe> provider) {
        String id = "%s_%s_".formatted(FormattingUtil.toLowerCaseUnder(dustPrefix.name), mat.getName().toLowerCase(Locale.ROOT));
        ItemStack dustStack = ChemicalHelper.get(dustPrefix, mat);
        OreProperty oreProperty = mat.hasProperty(PropertyKey.ORE) ? mat.getProperty(PropertyKey.ORE) : null;
        if (mat.hasProperty(PropertyKey.GEM)) {
            ItemStack gemStack = ChemicalHelper.get(gem, mat);

            if (mat.hasFlag(CRYSTALLIZABLE) || AOConfigHolder.INSTANCE.recipes.autoclaveProcessAllDust) {
                AUTOCLAVE_RECIPES.recipeBuilder("autoclave_" + id + "_water")
                    .inputItems(dustStack)
                    .inputFluids(Water.getFluid(250))
                    .chancedOutput(gemStack, 7000, 1000)
                    .duration(1200).EUt(24)
                    .save(provider);

                AUTOCLAVE_RECIPES.recipeBuilder("autoclave_" + id + "_distilled")
                    .inputItems(dustStack)
                    .inputFluids(DistilledWater.getFluid(50))
                    .outputItems(gemStack)
                    .duration(600).EUt(24)
                    .save(provider);
            }

            if (!mat.hasFlag(EXPLOSIVE) && !mat.hasFlag(FLAMMABLE)) {
                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_powderbarrel")
                    .inputItems(GTUtil.copyAmount(4, dustStack))
                    .outputItems(GTUtil.copyAmount(3, gemStack))
                    .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                    .explosivesType(new ItemStack(GTBlocks.POWDERBARREL, 8))
                    .save(provider);

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_tnt")
                    .inputItems(GTUtil.copyAmount(4, dustStack))
                    .outputItems(GTUtil.copyAmount(3, gemStack))
                    .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                    .explosivesAmount(4)
                    .save(provider);

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_dynamite")
                    .inputItems(GTUtil.copyAmount(4, dustStack))
                    .outputItems(GTUtil.copyAmount(3, gemStack))
                    .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                    .explosivesType(GTItems.DYNAMITE.asStack(2))
                    .save(provider);

                IMPLOSION_RECIPES.recipeBuilder("implode_" + id + "_itnt")
                    .inputItems(GTUtil.copyAmount(4, dustStack))
                    .outputItems(GTUtil.copyAmount(3, gemStack))
                    .chancedOutput(dust, GTMaterials.DarkAsh, 2500, 0)
                    .explosivesType(new ItemStack(GTBlocks.INDUSTRIAL_TNT))
                    .save(provider);
            }

            if (oreProperty != null) {
                Material smeltingResult = oreProperty.getDirectSmeltResult();
                if (smeltingResult != null) {
                    VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_ingot",
                        ChemicalHelper.getTag(dustPrefix, mat), ChemicalHelper.get(ingot, smeltingResult));
                }
            }

        } else if (mat.hasProperty(PropertyKey.INGOT)) {
            if (!mat.hasAnyOfFlags(FLAMMABLE, NO_SMELTING)) {

                boolean hasHotIngot = ingotHot.doGenerateItem(mat);
                ItemStack ingotStack = ChemicalHelper.get(hasHotIngot ? ingotHot : ingot, mat);
                if (ingotStack.isEmpty() && oreProperty != null) {
                    Material smeltingResult = oreProperty.getDirectSmeltResult();
                    if (smeltingResult != null) {
                        ingotStack = ChemicalHelper.get(ingot, smeltingResult);
                    }
                }
                int blastTemp = mat.getBlastTemperature();

                if (blastTemp <= 0) {
                    // smelting magnetic dusts is handled elsewhere
                    if (!mat.hasFlag(IS_MAGNETIC)) {
                        // do not register inputs by ore dict here. Let other mods register their own dust -> ingots
                        VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_demagnetize_from_dust",
                            ChemicalHelper.getTag(dustPrefix, mat), ingotStack);
                    }
                } else {
                    IngotProperty ingotProperty = mat.getProperty(PropertyKey.INGOT);
                    BlastProperty blastProperty = mat.getProperty(PropertyKey.BLAST);

                    processEBFRecipe(mat, blastProperty, ingotStack, provider);

                    if (ingotProperty.getMagneticMaterial() != null) {
                        processEBFRecipe(ingotProperty.getMagneticMaterial(), blastProperty, ingotStack, provider);
                    }
                }
            }
        } else {
            if (mat.hasFlag(GENERATE_PLATE) && !mat.hasFlag(EXCLUDE_PLATE_COMPRESSOR_RECIPE)) {
                COMPRESSOR_RECIPES.recipeBuilder("compress_plate_" + id)
                    .inputItems(dustStack)
                    .outputItems(plate, mat)
                    .save(provider);
            }

            // Some Ores with Direct Smelting Results have neither ingot nor gem properties
            if (oreProperty != null) {
                Material smeltingResult = oreProperty.getDirectSmeltResult();
                if (smeltingResult != null) {
                    ItemStack ingotStack = ChemicalHelper.get(ingot, smeltingResult);
                    if (!ingotStack.isEmpty()) {
                        VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_dust_to_ingot",
                            ChemicalHelper.getTag(dustPrefix, mat), ingotStack);
                    }
                }
            }
        }
    }

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
