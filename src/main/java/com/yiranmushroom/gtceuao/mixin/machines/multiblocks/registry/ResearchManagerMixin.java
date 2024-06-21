package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.yiranmushroom.gtceuao.recipes.AORecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ResearchManager.class)
public class ResearchManagerMixin {
    @Inject(method = "createDefaultResearchRecipe(Lcom/gregtechceu/gtceu/data/recipe/builder/GTRecipeBuilder;Ljava/util/function/Consumer;)V", at = @At("RETURN"), remap = false)
    private static void createDefaultResearchRecipeInj(@NotNull GTRecipeBuilder builder, Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        AORecipeTypes.registerAdvancedPrecisionAssemblyFromAssemblyLine(builder, provider);
    }
}
