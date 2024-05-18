package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.mojang.datafixers.util.Pair;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IntersectionIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.HIGH_SIFTER_OUTPUT;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ore;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(OreRecipeHandler.class)
public class OreRecipeHandlerMixin {
    @Shadow(remap = false)
    private static boolean doesMaterialUseNormalFurnace(Material material) {
        return !material.hasProperty(PropertyKey.BLAST);
    }

    @Inject(method = "processOre", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {

            Material byproductMaterial = GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
            ItemStack ingotStack;
            ItemStack byproductStack = ChemicalHelper.get(gem, byproductMaterial);
            if (byproductStack.isEmpty()) byproductStack = ChemicalHelper.get(dust, byproductMaterial);
            Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
            ItemStack rawStack = ChemicalHelper.get(rawOre, material);
            int amountOfCrushedOre = property.getOreMultiplier();
            if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
                ingotStack = ChemicalHelper.get(ingot, smeltingMaterial);
            } else if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
                ingotStack = ChemicalHelper.get(gem, smeltingMaterial);
            } else {
                ingotStack = ChemicalHelper.get(dust, smeltingMaterial);
            }
            int oreTypeMultiplier = TagPrefix.ORES.get(orePrefix).isDoubleDrops() ? 2 : 1;
            ingotStack.setCount(ingotStack.getCount() * property.getOreMultiplier() * oreTypeMultiplier);
            rawStack.setCount(rawStack.getCount() * property.getOreMultiplier());

            String prefixString = orePrefix == ore ? "" : orePrefix.name + "_";
            if (!rawStack.isEmpty()) {
                GTRecipeBuilder builder = MACERATOR_RECIPES.recipeBuilder("macerate_" + prefixString + material.getName() + "_ore_to_raw_ore")
                    .inputItems(IntersectionIngredient.of(Ingredient.of(orePrefix.getItemTags(material)[0]), Ingredient.of(orePrefix.getItemParentTags()[0])))
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre * 2 * oreTypeMultiplier, rawStack), 5000, 1600)
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre * 2 * oreTypeMultiplier, rawStack), 2500, 800)
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre * 2 * oreTypeMultiplier * 2, rawStack), 1250, 400)
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre * 2 * oreTypeMultiplier * 2, ChemicalHelper.get(dust, material)), 1400, 850)
                    .EUt(2)
                    .duration(400);

                builder.save(provider);
            }

            if (!ingotStack.isEmpty() && doesMaterialUseNormalFurnace(smeltingMaterial) && !orePrefix.isIgnored(material)) {
                float xp = Math.round(((1 + oreTypeMultiplier * 0.5f) * 0.5f - 0.05f) * 10f) / 10f;
                VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + prefixString + material.getName() + "_ore_to_ingot",
                    IntersectionIngredient.of(Ingredient.of(orePrefix.getItemTags(material)[0]), Ingredient.of(orePrefix.getItemParentTags()[0])), ingotStack, xp);
                VanillaRecipeHelper.addBlastingRecipe(provider, "smelt_" + prefixString + material.getName() + "_ore_to_ingot",
                    IntersectionIngredient.of(Ingredient.of(orePrefix.getItemTags(material)[0]), Ingredient.of(orePrefix.getItemParentTags()[0])), ingotStack, xp);
            }

            ci.cancel();
        }

    }

    @Inject(method = "processRawOre", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processRawOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {
            ItemStack crushedStack = ChemicalHelper.get(crushed, material, 2);
            ItemStack ingotStack;
            ItemStack dustStack = ChemicalHelper.get(dust, material, 2);
            Material byproductMaterial = GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
            ItemStack byproductStack = ChemicalHelper.get(gem, byproductMaterial, 2);
            if (byproductStack.isEmpty()) byproductStack = ChemicalHelper.get(dust, byproductMaterial, 2);
            Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
            if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
                ingotStack = ChemicalHelper.get(ingot, smeltingMaterial);
            } else if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
                ingotStack = ChemicalHelper.get(gem, smeltingMaterial);
            } else {
                ingotStack = ChemicalHelper.get(dust, smeltingMaterial);
            }

            if (!crushedStack.isEmpty()) {

                GTRecipeBuilder builder = FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + orePrefix.name + "_" + material.getName() + "_to_crushed_ore")
                    .inputItems(orePrefix, material)
                    .duration(10).EUt(16);
                if (material.hasProperty(PropertyKey.GEM) && !gem.isIgnored(material)) {
                    builder.outputItems(ChemicalHelper.get(gem, material, crushedStack.getCount()));
                } else {
                    builder.outputItems(crushedStack.copy());
                }
                builder.save(provider);

                MACERATOR_RECIPES.recipeBuilder("macerate_" + orePrefix.name + "_" + material.getName() + "_ore_to_crushed_ore")
                    .inputItems(orePrefix, material)
                    .outputItems(crushedStack)
                    .chancedOutput(crushedStack, 5000, 750)
                    .chancedOutput(dustStack, 5000, 750)
                    .chancedOutput(byproductStack, 1250, 500)
                    .EUt(2)
                    .duration(400)
                    .save(provider);

            }

            //do not try to add smelting recipes for materials which require blast furnace, or don't have smelting recipes at all.
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

            ci.cancel();
        }
    }

    @Inject(method = "processOreForgeHammer", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processOreForgeHammer(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {

            ItemStack crushedStack = ChemicalHelper.get(crushed, material);
            int amountOfCrushedOre = property.getOreMultiplier();
            int oreTypeMultiplier = TagPrefix.ORES.get(orePrefix).isDoubleDrops() ? 2 : 1;
            crushedStack.setCount(crushedStack.getCount() * property.getOreMultiplier());

            String prefixString = orePrefix == ore ? "" : orePrefix.name + "_";
            GTRecipeBuilder builder = FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + prefixString + material.getName() + "_ore_to_raw_ore")
                .inputItems(orePrefix, material)
                .duration(10).EUt(16);
            if (material.hasProperty(PropertyKey.GEM) && !gem.isIgnored(material)) {
                builder.outputItems(GTUtil.copyAmount(4 * amountOfCrushedOre * oreTypeMultiplier, ChemicalHelper.get(gem, material, crushedStack.getCount())));
            } else {
                builder.outputItems(GTUtil.copyAmount(4 * amountOfCrushedOre * oreTypeMultiplier, crushedStack));
            }

            ci.cancel();
        }
    }

    @Inject(method = "processCrushedCentrifuged", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processCrushedCentrifuged(TagPrefix centrifugedPrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {
            ItemStack dustStack = ChemicalHelper.get(dust, material, 3);
            ItemStack byproductStack = ChemicalHelper.get(dust, GTUtil.selectItemInList(2,
                material, property.getOreByProducts(), Material.class), 1);

            FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + material.getName() + "_refined_ore_to_dust")
                .inputItems(centrifugedPrefix, material)
                .outputItems(dustStack)
                .duration(10).EUt(16)
                .save(provider);

            MACERATOR_RECIPES.recipeBuilder("macerate_" + material.getName() + "_refined_ore_to_dust")
                .inputItems(centrifugedPrefix, material)
                .outputItems(dustStack)
                .chancedOutput(byproductStack, 1400, 850)
                .duration(400).EUt(2)
                .save(provider);

            VanillaRecipeHelper.addShapelessRecipe(provider, String.format("centrifuged_ore_to_dust_%s", material.getName()), dustStack,
                'h', new UnificationEntry(centrifugedPrefix, material));

            processMetalSmelting(centrifugedPrefix, material, property, provider);

            ci.cancel();
        }
    }

    @Inject(method = "processDirtyDust", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processDirtyDust(TagPrefix dustPrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {
            ItemStack dustStack = ChemicalHelper.get(dust, material, 2);

            Material byproduct = GTUtil.selectItemInList(
                0, material, property.getOreByProducts(), Material.class);

            GTRecipeBuilder builder = CENTRIFUGE_RECIPES.recipeBuilder("centrifuge_" + material.getName() + "_dirty_dust_to_dust")
                .inputItems(dustPrefix, material)
                .outputItems(dustStack)
                .duration((int) (material.getMass() * 4)).EUt(24);

            if (byproduct.hasProperty(PropertyKey.DUST)) {
                builder.chancedOutput(TagPrefix.dust, byproduct, 1111, 0);
            } else {
                builder.outputFluids(byproduct.getFluid(L / 9));
            }

            builder.save(provider);

            ORE_WASHER_RECIPES.recipeBuilder("wash_" + material.getName() + "_dirty_dust_to_dust")
                .inputItems(dustPrefix, material)
                .circuitMeta(2)
                .inputFluids(Water.getFluid(100))
                .outputItems(dustStack)
                .duration(8).EUt(4).save(provider);

            //dust gains same amount of material as normal dust
            processMetalSmelting(dustPrefix, material, property, provider);
            ci.cancel();
        }
    }

    @Inject(method = "processPureDust", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processPureDust(TagPrefix purePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {
            Material byproductMaterial = GTUtil.selectItemInList(
                1, material, property.getOreByProducts(), Material.class);
            ItemStack dustStack = ChemicalHelper.get(dust, material, 2);

            if (property.getSeparatedInto() != null && !property.getSeparatedInto().isEmpty()) {
                List<Material> separatedMaterial = property.getSeparatedInto();
                TagPrefix prefix = (separatedMaterial.get(separatedMaterial.size() - 1).getBlastTemperature() == 0 && separatedMaterial.get(separatedMaterial.size() - 1).hasProperty(PropertyKey.INGOT))
                    ? nugget : dust;

                ItemStack separatedStack2 = ChemicalHelper.get(prefix, separatedMaterial.get(separatedMaterial.size() - 1), prefix == nugget ? 2 : 1);

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("separate_" + material.getName() + "_pure_dust_to_dust")
                    .inputItems(purePrefix, material)
                    .outputItems(dustStack)
                    .chancedOutput(TagPrefix.dust, separatedMaterial.get(0), 1000, 250)
                    .chancedOutput(separatedStack2, prefix == TagPrefix.dust ? 500 : 2000, prefix == TagPrefix.dust ? 150 : 600)
                    .duration(200).EUt(24)
                    .save(provider);
            }

            CENTRIFUGE_RECIPES.recipeBuilder("centrifuge_" + material.getName() + "_pure_dust_to_dust")
                .inputItems(purePrefix, material)
                .outputItems(dustStack)
                .chancedOutput(TagPrefix.dust, byproductMaterial, 1111, 0)
                .duration(100)
                .EUt(5)
                .save(provider);

            ORE_WASHER_RECIPES.recipeBuilder("wash_" + material.getName() + "_pure_dust_to_dust")
                .inputItems(purePrefix, material)
                .circuitMeta(2)
                .inputFluids(Water.getFluid(100))
                .outputItems(dustStack)
                .duration(8).EUt(4).save(provider);

            processMetalSmelting(purePrefix, material, property, provider);
            ci.cancel();
        }
    }

    @Inject(method = "processCrushedPurified", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processCrushedPurified(TagPrefix purifiedPrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {
            ItemStack crushedCentrifugedStack = ChemicalHelper.get(crushedRefined, material);
            ItemStack dustStack = ChemicalHelper.get(dustPure, material);
            Material byproductMaterial = GTUtil.selectItemInList(
                1, material, property.getOreByProducts(), Material.class);
            ItemStack byproductStack = ChemicalHelper.get(dust, byproductMaterial);

            FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + material.getName() + "_crushed_ore_to_dust")
                .inputItems(purifiedPrefix, material)
                .outputItems(dustStack)
                .duration(10)
                .EUt(16)
                .save(provider);

            MACERATOR_RECIPES.recipeBuilder("macerate_" + material.getName() + "_crushed_ore_to_dust")
                .inputItems(purifiedPrefix, material)
                .outputItems(dustStack)
                .chancedOutput(byproductStack, 1400, 850)
                .duration(400).EUt(2)
                .save(provider);

            VanillaRecipeHelper.addShapelessRecipe(provider, String.format("purified_ore_to_dust_%s", material.getName()), dustStack,
                'h', new UnificationEntry(purifiedPrefix, material));

            if (!crushedCentrifugedStack.isEmpty()) {
                THERMAL_CENTRIFUGE_RECIPES.recipeBuilder("centrifuge_" + material.getName() + "_purified_ore_to_refined_ore")
                    .inputItems(purifiedPrefix, material)
                    .outputItems(crushedCentrifugedStack)
                    .chancedOutput(TagPrefix.dust, byproductMaterial, 3333, 0)
                    .save(provider);
            }

            if (material.hasProperty(PropertyKey.GEM)) {
                ItemStack exquisiteStack = ChemicalHelper.get(gemExquisite, material);
                ItemStack flawlessStack = ChemicalHelper.get(gemFlawless, material, 2);
                ItemStack gemStack = ChemicalHelper.get(gem, material, 4);
                ItemStack flawedStack = ChemicalHelper.get(gem, material, 2);
                ItemStack chippedStack = ChemicalHelper.get(gem, material, 1);

                dustStack.setCount(2);

                if (material.hasFlag(HIGH_SIFTER_OUTPUT)) {
                    GTRecipeBuilder builder = SIFTER_RECIPES.recipeBuilder("sift_" + material.getName() + "_purified_ore_to_gems")
                        .inputItems(purifiedPrefix, material)
                        .chancedOutput(exquisiteStack, 500, 150)
                        .chancedOutput(flawlessStack, 1500, 200)
                        .chancedOutput(gemStack, 5000, 1000)
                        .chancedOutput(dustStack, 2500, 500)
                        .duration(400).EUt(16);

                    if (!flawedStack.isEmpty())
                        builder.chancedOutput(flawedStack, 2000, 500);
                    if (!chippedStack.isEmpty())
                        builder.chancedOutput(chippedStack, 3000, 350);

                    builder.save(provider);
                } else {
                    GTRecipeBuilder builder = SIFTER_RECIPES.recipeBuilder("sift_" + material.getName() + "_purified_ore_to_gems")
                        .inputItems(purifiedPrefix, material)
                        .chancedOutput(exquisiteStack, 300, 100)
                        .chancedOutput(flawlessStack, 1000, 150)
                        .chancedOutput(gemStack, 3500, 500)
                        .chancedOutput(dustStack, 5000, 750)
                        .duration(400).EUt(16);

                    if (!flawedStack.isEmpty())
                        builder.chancedOutput(flawedStack, 2500, 300);
                    if (!chippedStack.isEmpty())
                        builder.chancedOutput(chippedStack, 3500, 400);

                    builder.save(provider);
                }

                dustStack.setCount(1);
            }
            processMetalSmelting(purifiedPrefix, material, property, provider);
            ci.cancel();
        }
    }

    @Shadow(remap = false)
    private static void processMetalSmelting(TagPrefix crushedPrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider) {

    }

}
