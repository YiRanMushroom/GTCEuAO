package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(OreRecipeHandler.class)
public class OreRecipeHandlerMixin {
    @Shadow(remap = false)
    private static boolean doesMaterialUseNormalFurnace(Material material) {
        return !material.hasProperty(PropertyKey.BLAST);
    }

    @Inject(method = "processOre", at = @At("RETURN"), remap = false, cancellable = true)
    private static void processOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (!AOConfigHolder.INSTANCE.recipes.buffOreYield) ci.cancel();

        Material byproductMaterial = GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
        ItemStack ingotStack;
        ItemStack byproductStack = ChemicalHelper.get(gem, byproductMaterial);
        if (byproductStack.isEmpty()) byproductStack = ChemicalHelper.get(dust, byproductMaterial);
        Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
        ItemStack rawOreStack = ChemicalHelper.get(rawOre, material);

        int oreMultiplier = TagPrefix.ORES.get(orePrefix).isDoubleDrops() ? 2 : 1;

        int amountOfCrushedOre = property.getOreMultiplier();

        if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
            ingotStack = ChemicalHelper.get(ingot, smeltingMaterial);
        } else if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
            ingotStack = ChemicalHelper.get(gem, smeltingMaterial);
        } else {
            ingotStack = ChemicalHelper.get(dust, smeltingMaterial);
        }
        ingotStack.setCount(ingotStack.getCount() * oreMultiplier * amountOfCrushedOre);

        String prefixString = orePrefix == ore ? "" : orePrefix.name + "_";
        if (!rawOreStack.isEmpty()) {
            GTRecipeBuilder builder = FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + prefixString + material.getName() + "_ore_to_raw_ore")
                    .inputItems(orePrefix, material)
                    .duration(10).EUt(16);
            if (material.hasProperty(PropertyKey.GEM) && !gem.isIgnored(material)) {
                builder.outputItems(GTUtil.copyAmount(oreMultiplier, ChemicalHelper.get(gem, material, rawOreStack.getCount())));
            } else {
                builder.outputItems(GTUtil.copyAmount(oreMultiplier, rawOreStack));
            }
            builder.save(provider);

            MACERATOR_RECIPES.recipeBuilder("macerate_" + prefixString + material.getName() + "_ore_to_raw_ore")
                    .inputItems(orePrefix, material)
                    .chancedOutput(GTUtil.copyAmount(oreMultiplier * 4, rawOreStack), 7500, 1000)
                    .chancedOutput(GTUtil.copyAmount(oreMultiplier * 4, rawOreStack), 5000, 500)
                    .chancedOutput(GTUtil.copyAmount(oreMultiplier * 8, rawOreStack), 2500, 250)
                    .chancedOutput(GTUtil.copyAmount(oreMultiplier * 4, byproductStack), 1250, 125)
                    .EUt(2)
                    .duration(400)
                    .save(provider);
        } else {
            ItemStack crushedStack = ChemicalHelper.get(crushed, material);

            GTRecipeBuilder builder = FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + prefixString + material.getName() + "_ore_to_crushed_ore")
                    .inputItems(orePrefix, material)
                    .duration(10).EUt(16);
            if (material.hasProperty(PropertyKey.GEM) && !gem.isIgnored(material)) {
                builder.outputItems(GTUtil.copyAmount(oreMultiplier, ChemicalHelper.get(gem, material, rawOreStack.getCount())));
            } else {
                builder.outputItems(GTUtil.copyAmount(oreMultiplier * amountOfCrushedOre, crushedStack));
            }
            builder.save(provider);

            MACERATOR_RECIPES.recipeBuilder("macerate_" + prefixString + material.getName() + "_ore_to_raw_ore")
                    .inputItems(orePrefix, material)
                    .chancedOutput(GTUtil.copyAmount(Math.min(64, oreMultiplier * amountOfCrushedOre * 4), crushedStack), 7500, 1000)
                    .chancedOutput(GTUtil.copyAmount(Math.min(64, oreMultiplier * amountOfCrushedOre * 4), crushedStack), 5000, 500)
                    .chancedOutput(GTUtil.copyAmount(Math.min(64, oreMultiplier * amountOfCrushedOre * 8), crushedStack), 2500, 250)
                    .chancedOutput(GTUtil.copyAmount(Math.min(64, oreMultiplier * amountOfCrushedOre * 4), byproductStack), 1250, 125)
                    .EUt(2)
                    .duration(400)
                    .save(provider);
        }

        //do not try to add smelting recipes for materials which require blast furnace
        if (!ingotStack.isEmpty() && doesMaterialUseNormalFurnace(smeltingMaterial) && !orePrefix.isIgnored(material)) {
            float xp = Math.round(((1 + oreMultiplier * 0.5f) * 0.5f - 0.05f) * 10f) / 10f;
            VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + prefixString + material.getName() + "_ore_to_ingot",
                    ChemicalHelper.getTag(orePrefix, material), ingotStack, xp);
            VanillaRecipeHelper.addBlastingRecipe(provider, "smelt_" + prefixString + material.getName() + "_ore_to_ingot",
                    ChemicalHelper.getTag(orePrefix, material), ingotStack, xp);
        }
    }

    @Inject(method = "processRawOre", at = @At("RETURN"), remap = false, cancellable = true)
    private static void processRawOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (!AOConfigHolder.INSTANCE.recipes.buffOreYield) ci.cancel();

        ItemStack crushedStack = ChemicalHelper.get(crushed, material);
        ItemStack ingotStack;
        Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
        int amountOfCrushedOre = property.getOreMultiplier();
        if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
            ingotStack = ChemicalHelper.get(ingot, smeltingMaterial, amountOfCrushedOre);
        } else if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
            ingotStack = ChemicalHelper.get(gem, smeltingMaterial, amountOfCrushedOre);
        } else {
            ingotStack = ChemicalHelper.get(dust, smeltingMaterial, amountOfCrushedOre);
        }

        if (!crushedStack.isEmpty()) {
            GTRecipeBuilder builder = FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + orePrefix.name + "_" + material.getName() + "_to_crushed_ore")
                    .inputItems(orePrefix, material)
                    .duration(10).EUt(16);
            if (material.hasProperty(PropertyKey.GEM) && !gem.isIgnored(material)) {
                builder.outputItems(GTUtil.copyAmount(amountOfCrushedOre, ChemicalHelper.get(gem, material, crushedStack.getCount())));
            } else {
                builder.outputItems(GTUtil.copyAmount(amountOfCrushedOre, crushedStack));
            }
            builder.save(provider);

            MACERATOR_RECIPES.recipeBuilder("macerate_" + orePrefix.name + "_" + material.getName() + "_ore_to_crushed_ore")
                    .inputItems(orePrefix, material)
                    .outputItems(GTUtil.copyAmount(amountOfCrushedOre, crushedStack))
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre, crushedStack), 5000, 750)
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre, crushedStack), 2500, 500)
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre, crushedStack), 1250, 250)
                    .EUt(2)
                    .duration(400)
                    .save(provider);
        }

        //do not try to add smelting recipes for materials which require blast furnace
        if (!ingotStack.isEmpty() && doesMaterialUseNormalFurnace(smeltingMaterial) && !orePrefix.isIgnored(material)) {
            float xp = Math.round(((1 + property.getOreMultiplier() * 0.33f) / 3) * 10f) / 10f;
            VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + orePrefix.name + "_" + material.getName() + "_ore_to_ingot",
                    ChemicalHelper.getTag(orePrefix, material), ingotStack, xp);
            VanillaRecipeHelper.addBlastingRecipe(provider, "smelt_" + orePrefix.name + "_" + material.getName() + "_ore_to_ingot",
                    ChemicalHelper.getTag(orePrefix, material), ingotStack, xp);
        }

        if (!ConfigHolder.INSTANCE.recipes.disableManualCompression) {
            VanillaRecipeHelper.addShapedRecipe(provider, "compress_" + material.getName() + "_to_ore_block",
                    ChemicalHelper.get(rawOreBlock, material),
                    "BBB", "BBB", "BBB",
                    'B', ChemicalHelper.getTag(rawOre, material));
            VanillaRecipeHelper.addShapelessRecipe(provider, "decompress_" + material.getName() + "_from_ore_block",
                    ChemicalHelper.get(rawOre, material, 9),
                    ChemicalHelper.getTag(rawOreBlock, material));
            COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "to_ore_block")
                    .inputItems(rawOre, material, 9)
                    .outputItems(rawOreBlock, material)
                    .duration(300).EUt(2).save(provider);
        }
    }
}