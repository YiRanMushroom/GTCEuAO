package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;
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


import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ore;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(OreRecipeHandler.class)
public class OreRecipeHandlerMixin {

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {

//        LOGGER.info("Patching OreRecipeHandler...");

        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {

//            LOGGER.info("Patching OreRecipeHandler: buffOreYield");

            for (TagPrefix ore : ORES.keySet()) {
                if (ConfigHolder.INSTANCE.worldgen.allUniqueStoneTypes || ORES.get(ore).shouldDropAsItem()) {
                    ore.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processOre(tagPrefix, material, property, provider));
                    ore.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processOreForgeHammer(tagPrefix, material, property, provider));
                }
            }

            rawOre.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processRawOre(tagPrefix, material, property, provider));

            crushed.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processCrushedOre(tagPrefix, material, property, provider));
            crushedPurified.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processCrushedPurified(tagPrefix, material, property, provider));
            crushedRefined.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processCrushedCentrifuged(tagPrefix, material, property, provider));
            dustImpure.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processDirtyDust(tagPrefix, material, property, provider));
            dustPure.executeHandler(PropertyKey.ORE, (tagPrefix, material, property) -> com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler.processPureDust(tagPrefix, material, property, provider));

            ci.cancel();
        }
    }

    @Shadow(remap = false)
    private static boolean doesMaterialUseNormalFurnace(Material material) {
        return !material.hasProperty(PropertyKey.BLAST);
    }

    @Inject(method = "processOre", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {

//            LOGGER.info("Patching OreRecipeHandler: processOre");

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
                    .chancedOutput(GTUtil.copyAmount(amountOfCrushedOre * 2 * oreTypeMultiplier * 2, byproductStack), 1400, 850)
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

//            LOGGER.info("Exiting OreRecipeHandler: processOre");

            ci.cancel();
        }

    }

    @Inject(method = "processRawOre", at = @At("HEAD"), remap = false, cancellable = true)
    private static void processRawOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.buffOreYield) {
            ItemStack crushedStack = ChemicalHelper.get(crushed, material);
            ItemStack ingotStack;
            Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
            if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
                ingotStack = ChemicalHelper.get(ingot, smeltingMaterial);
            } else if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
                ingotStack = ChemicalHelper.get(gem, smeltingMaterial);
            } else {
                ingotStack = ChemicalHelper.get(dust, smeltingMaterial);
            }

            if (!crushedStack.isEmpty()) {

                crushedStack.setCount(crushedStack.getCount() * 4);

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
                    .chancedOutput(crushedStack, 2500, 500)
                    .chancedOutput(crushedStack, 1250, 250)
                    .EUt(2)
                    .duration(400)
                    .save(provider);

                crushedStack.setCount(crushedStack.getCount() / 4);
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

            ItemStack rawStack = ChemicalHelper.get(rawOre, material);
            int oreTypeMultiplier = TagPrefix.ORES.get(orePrefix).isDoubleDrops() ? 2 : 1;
            rawStack.setCount(rawStack.getCount() * property.getOreMultiplier() * 2 * oreTypeMultiplier);

            String prefixString = orePrefix == ore ? "" : orePrefix.name + "_";
            if (!rawStack.isEmpty()) {
                FORGE_HAMMER_RECIPES.recipeBuilder("hammer_" + prefixString + material.getName() + "_ore_to_raw_ore")
                    .inputItems(IntersectionIngredient.of(Ingredient.of(orePrefix.getItemTags(material)[0]), Ingredient.of(orePrefix.getItemParentTags()[0])))
                    .duration(10).EUt(16)
                    .outputItems(rawStack)
                    .save(provider);
            }

//            LOGGER.info("Exiting OreRecipeHandler: processOreForgeHammer");

            ci.cancel();
        }
    }
}
