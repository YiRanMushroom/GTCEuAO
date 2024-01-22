package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.google.common.collect.ImmutableMap;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.WireRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(WireRecipeHandler.class)
public class WireRecipeHandlerMixin {
    @Inject(method = "generateCableCovering", at = @At("HEAD"), cancellable = true, remap = false)
    private static void generateCableCovering(TagPrefix wirePrefix, Material material, WireProperties property, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        if (AOConfigHolder.INSTANCE.recipes.easierCableRecipes) {

            if (property.isSuperconductor()) return;

            int cableAmount = (int) (wirePrefix.getMaterialAmount(material) * 2 / M);
            TagPrefix cablePrefix = TagPrefix.get("cable" + wirePrefix.name().substring(4));
            int voltageTier = GTUtil.getTierByVoltage(property.getVoltage());
            int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);

            // Generate hand-crafting recipes for ULV and LV cables
            if (voltageTier <= MV) {
                generateManualRecipe(wirePrefix, material, cablePrefix, cableAmount, provider);
            }

            // Rubber Recipe (ULV-LuV cables)
            if (voltageTier <= LuV) {
                GTRecipeBuilder builder = ASSEMBLER_RECIPES.recipeBuilder("cover_" + material.getName() + "_" + wirePrefix + "_rubber")
                        .EUt(VA[ULV]).duration(100)
                        .inputItems(wirePrefix, material)
                        .outputItems(cablePrefix, material)
                        .inputFluids(Rubber.getFluid(L * (long) insulationAmount));

                if (voltageTier >= EV) {
                    builder.inputItems(foil, PolyvinylChloride, insulationAmount);
                }
                builder.save(provider);
            }

            // Silicone Rubber Recipe (all cables)
            GTRecipeBuilder builder = ASSEMBLER_RECIPES.recipeBuilder("cover_" + material.getName() + "_" + wirePrefix + "_silicone")
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material);

            // Apply a Polyphenylene Sulfate Foil if ZPM or above.
            if (voltageTier >= ZPM) {
                builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);
            }

            // Apply a PVC Foil if EV or above.
            if (voltageTier >= EV) {
                builder.inputItems(foil, PolyvinylChloride, insulationAmount);
            }

            builder.inputFluids(SiliconeRubber.getFluid(L * (long) insulationAmount / 2))
                    .save(provider);

            // Styrene Butadiene Rubber Recipe (all cables)
            builder = ASSEMBLER_RECIPES.recipeBuilder("cover_" + material.getName() + "_" + wirePrefix + "_styrene_butadiene")
                    .EUt(VA[ULV]).duration(100)
                    .inputItems(wirePrefix, material)
                    .outputItems(cablePrefix, material);

            // Apply a Polyphenylene Sulfate Foil if ZPM or above.
            if (voltageTier >= ZPM) {
                builder.inputItems(foil, PolyphenyleneSulfide, insulationAmount);
            }

            // Apply a PVC Foil if EV or above.
            if (voltageTier >= EV) {
                builder.inputItems(foil, PolyvinylChloride, insulationAmount);
            }

            builder.inputFluids(StyreneButadieneRubber.getFluid(L * (long) insulationAmount / 4))
                    .save(provider);

            ci.cancel();
        }
    }

    @Final
    @Shadow(remap = false)
    private static final Map<TagPrefix, Integer> INSULATION_AMOUNT = ImmutableMap.of(
            cableGtSingle, 1,
            cableGtDouble, 1,
            cableGtQuadruple, 2,
            cableGtOctal, 3,
            cableGtHex, 5
    );

    @Shadow(remap = false)
    private static void generateManualRecipe(TagPrefix wirePrefix, Material material, TagPrefix cablePrefix, int cableAmount, Consumer<FinishedRecipe> provider){
        int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);
        Object[] ingredients = new Object[insulationAmount + 1];
        ingredients[0] = new UnificationEntry(wirePrefix, material);
        for (int i = 1; i <= insulationAmount; i++) {
            ingredients[i] = ChemicalHelper.get(plate, Rubber);
        }
        VanillaRecipeHelper.addShapelessRecipe(provider, String.format("%s_cable_%d", material.getName(), cableAmount),
                ChemicalHelper.get(cablePrefix, material),
                ingredients
        );

        PACKER_RECIPES.recipeBuilder("cover_" + material.getName() + "_" + wirePrefix)
                .inputItems(wirePrefix, material)
                .inputItems(plate, Rubber, insulationAmount)
                .outputItems(cablePrefix, material)
                .duration(100).EUt(VA[ULV])
                .save(provider);
    }
}
