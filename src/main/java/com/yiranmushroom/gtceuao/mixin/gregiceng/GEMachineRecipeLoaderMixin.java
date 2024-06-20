package com.yiranmushroom.gtceuao.mixin.gregiceng;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.epimorphismmc.gregiceng.common.data.GEMachines;
import com.epimorphismmc.gregiceng.data.recipe.misc.MachineRecipeLoader;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.item.forge.GTBucketItem;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import net.minecraft.data.recipes.FinishedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.epimorphismmc.gregiceng.common.data.GEMachines.INPUT_BUFFER;
import static com.epimorphismmc.gregiceng.common.data.GEMachines.OUTPUT_BUFFER;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

@Mixin(value = MachineRecipeLoader.class, remap = false)
public class GEMachineRecipeLoaderMixin {
    @Inject(method = "init", at = @At("RETURN"), remap = false, require = 0)
    private static void initInj(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ASSEMBLER_RECIPES
            .recipeBuilder("crafting_io_buffer")
            .inputItems(CustomTags.MV_CIRCUITS)
            .inputItems(AEBlocks.PATTERN_PROVIDER.stack(3))
            .inputItems(GTMachines.HULL[MV])
            .outputItems(GEMachines.CRAFTING_IO_BUFFER.asStack())
            .duration(100)
            .EUt(VA[MV])
            .save(provider);
    }
}
