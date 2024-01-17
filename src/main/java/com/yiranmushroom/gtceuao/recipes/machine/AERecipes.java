package com.yiranmushroom.gtceuao.recipes.machine;

import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityMachineRecipeLoader;
import com.gregtechceu.gtceu.integration.ae2.GTAEMachines;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class AERecipes {
    public static void register(Consumer<FinishedRecipe> provider) {
        if (AOConfigHolder.INSTANCE.recipes.AE2RecipeSupport&& GTCEu.isAE2Loaded())
            registerAE2(provider);

    }

    private static void registerAE2(Consumer<FinishedRecipe> provider) {
        ItemStack meInterface = AEParts.INTERFACE.stack(1);
//        ItemStack accelerationCard = AEItems.SPEED_CARD.stack(2);

        ASSEMBLER_RECIPES.recipeBuilder("me_export_hatch")
                .inputItems(FLUID_EXPORT_HATCH[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.FLUID_EXPORT_HATCH.asStack())
                .duration(300).EUt(VA[MV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("me_import_hatch")
                .inputItems(FLUID_IMPORT_HATCH[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.FLUID_IMPORT_HATCH.asStack())
                .duration(300).EUt(VA[MV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("me_export_bus")
                .inputItems(ITEM_EXPORT_BUS[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.ITEM_EXPORT_BUS.asStack())
                .duration(300).EUt(VA[MV]).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("me_import_bus")
                .inputItems(ITEM_IMPORT_BUS[MV])
                .inputItems(meInterface.copy())
                .outputItems(GTAEMachines.ITEM_IMPORT_BUS.asStack())
                .duration(300).EUt(VA[MV]).save(provider);
    }
}

/*
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;

@Mixin(MetaTileEntityMachineRecipeLoader.class)
public class MetaTileEntityMachineRecipeLoaderMixin {

}
*/

