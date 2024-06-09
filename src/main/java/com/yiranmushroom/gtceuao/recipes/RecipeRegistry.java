package com.yiranmushroom.gtceuao.recipes;

import com.yiranmushroom.gtceuao.recipes.machine.*;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class RecipeRegistry {
    public static void registerRecipes(Consumer<FinishedRecipe> provider) {
        CircuitRecipes.register(provider);
        AERecipes.register(provider);
        EasyChemistryRecipes.register(provider);
        MiscRecipes.register(provider);
        ExNihiloRecipe.register(provider);
        EasyOreProcessing.register(provider);
        OPNaquadahLineRecipes.register(provider);
        NewContent.register(provider);
    }
}
