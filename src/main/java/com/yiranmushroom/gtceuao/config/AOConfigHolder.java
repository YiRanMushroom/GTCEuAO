package com.yiranmushroom.gtceuao.config;

import com.yiranmushroom.gtceuao.gtceuao;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = gtceuao.MODID)
public class AOConfigHolder {
    public static AOConfigHolder INSTANCE;

    public static void init() {
        INSTANCE = Configuration.registerConfig(AOConfigHolder.class, ConfigFormats.yaml()).getConfigInstance();
    }

    @Configurable
    public MachineConfigs machines = new MachineConfigs();
    @Configurable
    public RecipeConfigs recipes = new RecipeConfigs();

    public static class MachineConfigs {

        @Configurable
        @Configurable.Comment({"Multiplier for Recipe Power Consumption per Overclock.", "Default: 1.0"})
        @Configurable.DecimalRange(min = 1.0, max = 4.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double overclockMultiplier = 1.0;

        @Configurable
        @Configurable.Comment({"Exponential of Perfect Overclocking.", "Default: 3.0"})
        @Configurable.DecimalRange(min = 1.0, max = 5.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double ExpPerfect = 3.0;

        @Configurable
        @Configurable.Comment({"Additional Times That Machines Are Allowed to Overclock, Still Confined by Voltage.", "Default: 1"})
        @Configurable.DecimalRange(min = 0, max = 128)
        public int bonusOfOCs = 1;

        @Configurable
        @Configurable.Comment({"Multiplier of Processing Array Parallel Amount.", "Default: 16"})
        @Configurable.DecimalRange(min = 1, max = 4096)
        public int PAPMultiplier = 16;

        @Configurable
        @Configurable.Comment({"Is Processing Arrays Have OP Chance of Output.", "Default: true"})
        public boolean PAHasOPChance = true;

        @Configurable
        @Configurable.Comment({"Is Processing Arrays Ignoring Machine Tier.",
                "This Will Affect Both Recipe Tier Check, and Tier That Recipes can Be Overclocked to.",
                "Default: true"})
        public boolean PAIgnoreTier = true;

        @Configurable
        @Configurable.Comment({"Do Parallel Need More Power to Operate, Rather than Use Original Recipe Power.",
                "Only Affect Input Energy",
                "Default: false"})
        public boolean ParallelNeedMorePower = false;
    }

    public static class RecipeConfigs {
        @Configurable
        @Configurable.Comment({"Is Processing Arrays Have Easier Recipes.",
                "Default: true"})
        public boolean EasierPARecipes = true;

        @Configurable
        @Configurable.Comment({"Do circuit assemblers need higher tier circuits to craft.", "Default: false"})
        public boolean circuitAssemblersNeedHigherTierCircuits = false;


    }


}
