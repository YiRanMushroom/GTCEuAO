package com.yiranmushroom.gtceuao.mixin.recipe.change.machineRecipes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.MaterialRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.misc.alloyblast.AlloyBlastRecipeProducer;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.gtceuao;
import dev.emi.emi.config.HelpLevel;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

@Mixin(AlloyBlastRecipeProducer.class)
public class AlloyBlastRecipeProducerMixin {
    /**
     * @author YiranMushroom
     * @reason Change molten metal freezing recipe
     */
    @Overwrite(remap = false)
    @SuppressWarnings("MethodMayBeStatic")
    protected void addFreezerRecipes(@NotNull Material material, @NotNull Fluid molten, int temperature, Consumer<FinishedRecipe> provider) {
        GTRecipeBuilder freezerBuilder;
        if (AOConfigHolder.INSTANCE.recipes.moltenCoolDownNeedFreezer) {
            freezerBuilder = GTRecipeTypes.VACUUM_RECIPES.recipeBuilder(material.getName())
                .inputFluids(FluidStack.create(molten, GTValues.L))
                .duration((int) material.getMass() * 3)
                .notConsumable(GTItems.SHAPE_MOLD_INGOT.asStack())
                .outputItems(TagPrefix.ingot, material);

            // helium for when >= 5000K temperature
            if (temperature >= 5000 && AOConfigHolder.INSTANCE.recipes.hotIngotNeedLiquidHelium) {
                freezerBuilder.inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 500))
                    .outputFluids(GTMaterials.Helium.getFluid(250));
            }
        } else
            freezerBuilder = FLUID_SOLIDFICATION_RECIPES.recipeBuilder(material.getName())
                .inputFluids(FluidStack.create(molten, L))
                .duration((int) material.getMass() * 3)
                .notConsumable(GTItems.SHAPE_MOLD_INGOT.asStack())
                .outputItems(ingot, material)
                .EUt(VA[MV]);
        freezerBuilder.save(provider);
    }
}

