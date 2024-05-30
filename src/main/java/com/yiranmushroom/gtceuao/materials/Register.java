package com.yiranmushroom.gtceuao.materials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class Register {
    public static void secondTierMaterialRegister() {
        if (AOConfigHolder.INSTANCE.recipes.exNihiloLikeRecipesAndMaterials)
            secondOrderMaterial();
    }

    public static void additionalMaterialFlagsRegister() {
        modifyOreProducts();
    }

    public static void higherOrderMaterialRegister() {
        if (AOConfigHolder.INSTANCE.recipes.exNihiloLikeRecipesAndMaterials)
            higherOrderMaterial();
    }

    private static void higherOrderMaterial() {

        Redrock = new Material.Builder(GTCEu.id("redrock"))
            .dust(1)
            .color(0xffa49e).secondaryColor(0x52362a).iconSet(ROUGH)
            .flags(NO_SMASHING, DECOMPOSITION_BY_CENTRIFUGING)
            .components(Calcite, 1, Rutile, 1)
            .buildAndRegister();

        Granite = new Material.Builder(GTCEu.id("granite"))
            .dust()
            .color(0xd69077).secondaryColor(0x71352c).iconSet(ROUGH)
            .flags(DECOMPOSITION_BY_CENTRIFUGING)
            .components(SiliconDioxide, 1, Redrock, 1, Fluorine, 2)
            .buildAndRegister();
    }

    private static void secondOrderMaterial() {
        // Second Degree Materials
        Clay = new Material.Builder(GTCEu.id("clay"))
            .dust(1)
            .color(0xbec9e8).secondaryColor(0x373944).iconSet(ROUGH)
            .flags(MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, DECOMPOSITION_BY_ELECTROLYZING)
            .components(Sodium, 2, Lithium, 1, Potassium, 1, Aluminium, 2, Silicon, 2, Iron, 2)
            .buildAndRegister();


        LeadZincSolution = new Material.Builder(GTCEu.id("lead_zinc_solution"))
            .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().customStill())
            .flags(DECOMPOSITION_BY_CENTRIFUGING)
            .components(Lead, 2, Silver, 2, Zinc, 2, Gallium, 1, Sulfur, 5, Water, 1)
            .buildAndRegister();
    }

    static OreProperty oreProp = new OreProperty();

    public static void modifyOreProducts() {
        if (!AOConfigHolder.INSTANCE.recipes.easierOreProcessing)
            return;

        oreProp = Palladium.getProperty(PropertyKey.ORE);
        oreProp.setOreByProducts(Iridium, Osmium, Rhodium, Platinum);
        oreProp.setWashedIn(Mercury);
    }
}
