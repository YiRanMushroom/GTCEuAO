package com.yiranmushroom.gtceuao.config;

import com.gregtechceu.gtceu.config.ConfigHolder;
import com.yiranmushroom.gtceuao.gtceuao;

import com.gregtechceu.gtceu.GTCEu;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = gtceuao.MODID)
public class MixinConfigHolder {
    public static MixinConfigHolder INSTANCE;

    public static void init() {
        INSTANCE = Configuration.registerConfig(MixinConfigHolder.class, ConfigFormats.yaml()).getConfigInstance();
    }

    @Configurable
    public MachineConfigs machines = new MachineConfigs();

    public static class MachineConfigs {

        @Configurable
        @Configurable.Comment({"Multiplier for Recipe Power Consumption per Overclock.", "Default: 1.0"})
        @Configurable.DecimalRange(min = 1.0, max = 2.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double overclockMultiplier = 1.0;

        @Configurable
        @Configurable.Comment({"Exponential of Perfect Overclocking.", "Default: 3.0"})
        @Configurable.DecimalRange(min = 1.0, max = 5.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double ExpPerfect = 3.0;

    }


}
