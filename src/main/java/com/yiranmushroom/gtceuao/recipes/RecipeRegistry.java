package com.yiranmushroom.gtceuao.recipes;

import com.yiranmushroom.gtceuao.recipes.machine.CircuitRecipes;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class RecipeRegistry {
    public static void registerRecipes(Consumer<FinishedRecipe> provider) {
        CircuitRecipes.register(provider);
    }
}
