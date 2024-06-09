package com.yiranmushroom.gtceuao.recipes;

import com.gregtechceu.gtceu.api.capability.recipe.CWURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;
import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

public class AORecipeTypes {
    public static void registerRecipeTypes() {
        ADVANCED_PRECISION_ASSEMBLY_Recipe = register("advanced_precision_assembly", MULTIBLOCK).setMaxIOSize(18, 6, 6, 0).setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .setMaxTooltips(4);
    }

    public static void registerAdvancedPrecisionAssemblyFromAssemblyLine(GTRecipeBuilder originalRecipeBuilder, Consumer<FinishedRecipe> provider) {
        GTRecipeBuilder recipeBuilder = ADVANCED_PRECISION_ASSEMBLY_Recipe.recipeBuilder(new ResourceLocation(originalRecipeBuilder.id.toString() + "_advanced"));
        LOGGER.info("Registering advanced precision assembly recipe: {}", recipeBuilder.id);
        recipeBuilder.input.putAll(originalRecipeBuilder.input);
        recipeBuilder.tickInput.putAll(originalRecipeBuilder.tickInput);
        recipeBuilder.tickInput.remove(CWURecipeCapability.CAP);
        recipeBuilder.output.putAll(originalRecipeBuilder.output);
        recipeBuilder.duration(originalRecipeBuilder.duration).save(provider);
    }

    public static GTRecipeType ADVANCED_PRECISION_ASSEMBLY_Recipe;
}
