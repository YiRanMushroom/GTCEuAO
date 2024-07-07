package com.yiranmushroom.gtceuao.mixin.config;

import com.gregtechceu.gtceu.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfigHolder.class)
public abstract class ConfigHolderMixin {
    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void initInj(CallbackInfo ci) {

    }

    @Mixin(ConfigHolder.MachineConfigs.class)
    public abstract static class machineConfigsMixin {
/*        // This is used to cheat the mixin system to prevent crash
        @Unique
        public double A_overclockDivisorProxy = 4.0;

        @Shadow(remap = false, aliases = {"overclockDivisorProxy"})
        @Configurable
        @Configurable.Comment({"Divisor for Recipe Duration per Overclock.", "Default: 4.0"})
        @Configurable.DecimalRange(min = 1.0, max = 256.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double overclockDivisor = 4.0;*/

        @Shadow(remap = false)
        @Configurable
        @Configurable.Comment({"Whether to enable the cleanroom, required for various recipes.", "Default: false"})
        public boolean enableCleanroom = false;

        @Shadow(remap = false)
        @Configurable
        @Configurable.Comment({"Whether to enable the Maintenance Hatch, required for Multiblocks.", "Default: false"})
        public boolean enableMaintenance = false;

        @Shadow(remap = false)
        @Configurable
        @Configurable.Comment({"Wether to add a \"Bedrock Ore Miner\" (also enables bedrock ore generation)",
            "This will also slightly change the recipe of the fluid drilling rig.",
            "Default: true"})
        public boolean doBedrockOres = true;

    }

    @Mixin(ConfigHolder.RecipeConfigs.class)
    public abstract static class recipeConfigsMixin {
        @Shadow(remap = false)
        @Configurable
        @Configurable.Comment({"Whether to remove Block/Ingot compression and decompression in the Crafting Table.", "Default: false"})
        public boolean disableManualCompression = false; // default true
    }
}


