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


    }

    @Mixin(ConfigHolder.CompatibilityConfigs.class)
    public abstract static class compatibilityConfigsMixin {
        @Mixin(ConfigHolder.CompatibilityConfigs.EnergyCompatConfig.class)
        public abstract static class energyCompatConfigMixin {

        }
    }
}


