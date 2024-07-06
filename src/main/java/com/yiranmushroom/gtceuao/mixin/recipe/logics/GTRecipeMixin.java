package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(GTRecipe.class)
public abstract class GTRecipeMixin implements Recipe<Container> {
    @Mutable
    @Final
    @Shadow(remap = false)
    public final GTRecipeType recipeType;

    @Mutable
    @Final
    @Shadow(remap = false)
    public final ResourceLocation id;
    @Mutable
    @Shadow(remap = false)
    @Final
    public final Map<RecipeCapability<?>, List<Content>> inputs;
    @Mutable
    @Shadow(remap = false)
    @Final
    public final Map<RecipeCapability<?>, List<Content>> outputs;
    @Mutable
    @Shadow(remap = false)
    @Final
    public final Map<RecipeCapability<?>, List<Content>> tickInputs;
    @Mutable
    @Shadow(remap = false)
    @Final
    public final Map<RecipeCapability<?>, List<Content>> tickOutputs;
    @Mutable
    @Shadow(remap = false)
    @Final
    public final List<RecipeCondition> conditions;
    // for KubeJS. actual type is List<IngredientAction>.
    // Must be List<?> to not cause crashes without KubeJS.
    @Mutable
    @Shadow(remap = false)
    @Final
    public final List<?> ingredientActions;
    @NotNull
    @Shadow(remap = false)
    public CompoundTag data;
    @Shadow(remap = false)
    public int duration;
    @Getter
    @Shadow(remap = false)
    public boolean isFuel;

    public GTRecipeMixin(GTRecipeType recipeType, ResourceLocation id, Map<RecipeCapability<?>, List<Content>> inputs, Map<RecipeCapability<?>, List<Content>> outputs, Map<RecipeCapability<?>, List<Content>> tickInputs, Map<RecipeCapability<?>, List<Content>> tickOutputs, List<RecipeCondition> conditions, List<?> ingredientActions) {
        this.recipeType = recipeType;
        this.id = id;
        this.inputs = inputs;
        this.outputs = outputs;
        this.tickInputs = tickInputs;
        this.tickOutputs = tickOutputs;
        this.conditions = conditions;
        this.ingredientActions = ingredientActions;
    }

    @Shadow(remap = false)
    public Map<RecipeCapability<?>, List<Content>> copyContents(Map<RecipeCapability<?>, List<Content>> contents, @Nullable ContentModifier modifier) {
        return null;
    }

/**
     * @author YiranMushroom
     * @reason Overwrite the copy method to make it not modify the voltage*/

    @Overwrite(remap = false)
    public GTRecipe copy(ContentModifier modifier, boolean modifyDuration) {
        var copied = new GTRecipe(recipeType, id, copyContents(inputs, modifier), copyContents(outputs, modifier),
            AOConfigHolder.INSTANCE.machines.ParallelNeedMorePower ? copyContents(tickInputs, modifier) : tickInputs,
            copyContents(tickOutputs, modifier), new ArrayList<>(conditions), new ArrayList<>(ingredientActions),
            data, duration, isFuel);
        if (modifyDuration) {
            copied.duration = modifier.apply(this.duration).intValue();
        }
        return copied;
    }
}
