package com.yiranmushroom.gtceuao.mixin.recipe.logics;

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import org.checkerframework.checker.signature.qual.FullyQualifiedName;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(GTRecipe.class)
public abstract class GTRecipeMixin implements Recipe<Container> {
    @Final
    @Shadow(remap = false)
    public GTRecipeType recipeType;

    @Final
    @Shadow(remap = false)
    public ResourceLocation id;
    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> inputs;
    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> outputs;
    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> tickInputs;
    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> tickOutputs;
    @Shadow(remap = false)
    @Final
    public List<RecipeCondition> conditions;
    // for KubeJS. actual type is List<IngredientAction>.
    // Must be List<?> to not cause crashes without KubeJS.
    @Shadow(remap = false)
    @Final
    public List<?> ingredientActions;
    @NotNull
    @Shadow(remap = false)
    public CompoundTag data;
    @Shadow(remap = false)
    public int duration;
    @Getter
    @Shadow(remap = false)
    public boolean isFuel;

    @Shadow(remap = false)
    public abstract Map<RecipeCapability<?>, List<Content>> copyContents(Map<RecipeCapability<?>, List<Content>> contents, @Nullable ContentModifier modifier);

/**
     * @author YiranMushroom
     * @reason Overwrite the copy method to make it not modify the voltage*/

    @Overwrite(remap = false)
    public GTRecipe copy(ContentModifier modifier, boolean modifyDuration) {
        var copied = new GTRecipe(recipeType, id, copyContents(inputs, modifier), copyContents(outputs, modifier),
            AOConfigHolder.INSTANCE.machines.ParallelNeedMorePower
                | RecipeHelper.getOutputEUt((GTRecipe) (Object)this) > 0
                | RecipeHelper.getInputEUt((GTRecipe) (Object)this) < 0
                ? copyContents(tickInputs, modifier) : copyContents(tickInputs, null),
            copyContents(tickOutputs, modifier), new ArrayList<>(conditions), new ArrayList<>(ingredientActions),
            data, duration, isFuel);
        if (modifyDuration) {
            copied.duration = modifier.apply(this.duration).intValue();
        }
        return copied;
    }
}
