package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.gregtechceu.gtceu.common.data.GTRecipes;
import com.yiranmushroom.gtceuao.recipes.RecipeRegistry;
import net.minecraft.data.recipes.FinishedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(GTRecipes.class)
public class GTRecipesMixin {
    @Inject(method = "recipeAddition", at = @At("RETURN"), remap = false)
    private static void recipeAddition(Consumer<FinishedRecipe> consumer, CallbackInfo ci) {
        RecipeRegistry.registerRecipes(consumer);
        LOGGER.info("Register recipes finished.");
    }
}
