package com.yiranmushroom.gtceuao.mixin.config;

import com.gregtechceu.gtceu.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ConfigHolder.class)
public abstract class ConfigHolderMixin {
    @Mixin(ConfigHolder.MachineConfigs.class)
    public abstract static class machineConfigsMixin {
        @Shadow(remap = false)
        @Configurable
        @Configurable.Comment({"Divisor for Recipe Duration per Overclock.", "Default: 4.0"})
        @Configurable.DecimalRange(min = 1.0, max = 256.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double overclockDivisor = 4.0;

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

    @Mixin(ConfigHolder.CompatibilityConfigs.AE2CompatConfig.class)
    public abstract static class ae2CompatConfigMixin {
        @Shadow(remap = false)
        @Configurable.Comment({"The interval between ME Hatch/Bus interact ME network.", "It may cause lag if the interval is too small.", "Default: 2 sec"})
        @Configurable.Range(min = 1, max = 80)
        public int updateIntervals = 20;

        @Shadow(remap = false)
        @Configurable.Comment({"The energy consumption of ME Hatch/Bus.", "Default: 1.0AE/t"})
        @Configurable.DecimalRange(min = 0.0, max = 10.0)
        public double meHatchEnergyUsage = 0.0;
    }

    @Mixin(ConfigHolder.CompatibilityConfigs.class)
    public abstract static class compatibilityConfigsMixin {
        @Mixin(ConfigHolder.CompatibilityConfigs.EnergyCompatConfig.class)
        public abstract static class energyCompatConfigMixin {

        }
    }

    @Mixin(ConfigHolder.RecipeConfigs.class)
    public abstract static class recipeConfigsMixin {
        @Shadow(remap = false)
        @Configurable
        @Configurable.Comment({"Whether to remove Block/Ingot compression and decompression in the Crafting Table.", "Default: false"})
        public boolean disableManualCompression = false; // default true
    }
}


