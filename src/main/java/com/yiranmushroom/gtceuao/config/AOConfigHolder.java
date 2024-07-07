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

    @Configurable
    public MiscConfigs misc = new MiscConfigs();

    public static class MachineConfigs {
        @Configurable
        @Configurable.Comment({"Divisor for Recipe Duration per Overclock. This will overwrite the GTCEu Config",
            "This is moved here to be compatible with a fork version of GTCEu.",
            "Default: 4.0"})
        @Configurable.DecimalRange(min = 1.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double overclockDivisor = 4.0;

        @Configurable
        @Configurable.Comment({"Divisor for Recipe Duration per Overclock. This will overwrite the GTCEu Config",
            "This is moved here to be compatible with a fork version of GTCEu.",
            "Default: 4.0"})
        @Configurable.DecimalRange(min = 1.0)
        @Configurable.Gui.NumberFormat("0.0#")
        public double perfectOverclockDivisor = 64.0;

        @Configurable
        @Configurable.Comment({"Is Cleanroom check disabled?",
            "Default: true"})
        public boolean disableCleanroomCheck = true;

        @Configurable
        @Configurable.Comment({"Is Native Platform Energy to EU enabled. Disable this may cause some unknown bugs.",
            "Default: true"})
        public boolean nativePEToEU = true;

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
        @Configurable.Comment({"Multiplier of Parallel Amount, this will affect Processing Array and Parallel Hatches", "Default: 16"})
        @Configurable.DecimalRange(min = 1, max = 4096)
        public int ParallelMultiplier = 16;

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
            "Only Affect Input Energy, and this will not affect parallel limit calculation (or it would be very laggy)",
            "Default: false"})
        public boolean ParallelNeedMorePower = false;

        @Configurable
        @Configurable.Comment({"Do Buff Buses and Hatches", "Because of bug of gregtech itself, the item slot cannot be too large, may fix in the future", "Default: true"})
        public boolean buffBusesAndHatches = true;

        @Configurable
        @Configurable.Comment({"Do Multiblocks Be Strongly Buffed, most of them would become parallelable.",
            "Due to technical issues, this will always be true, even if you set it to false.",
            "Default: true"})
        public boolean buffMultiblocks = true;

        @Configurable
        @Configurable.Comment({"In what amount should be non-coilblock multiblock machines parallel.", "Default: 16"})
        public int multiblockParallelAmount = 16;

        @Configurable
        @Configurable.Comment({"In what amount should be subtick parallel goto, too large may cause severe tps drop", "Default: 64"})
        public int subtickParallelLimit = 262144;

        @Configurable
        @Configurable.Comment({"Do bedrock drills deplete resources.", "Default: false"})
        public boolean bedrockDrillsDepleteResources = false;

        @Configurable
        @Configurable.Comment({"Do rock breaker don't need to satisfy conditions.", "Default: true"})
        public boolean rockBreakerIgnoreConditions = true;

        @Configurable
        @Configurable.Comment({"Do use legacy logic for parallel, which may reduce performance, but less buggy",
            "Default: true"})
        public boolean legacyParallelLogic = true;

/*        @Configurable
        @Configurable.Comment({"Do fast logic for parallel, which will make parallel not accurate, but way faster",
            "This will only be effective if legacy parallel logic is disabled.",
            "Default: true"})
        public boolean fastParallelLogic = false;*/

        @Configurable
        @Configurable.Comment({"Are turbines buffed, which will make the energy hatches determine the production, and holders determines the efficiency.",
            "Default: false"})
        public boolean buffTurbines = true;

        @Configurable
        @Configurable.Comment({"Whether to disable rotor damage from running.",
            "Default: false"})
        public boolean disableRotorDamage = true;

        @Configurable
        @Configurable.Comment({"Do make RecipeModifier not lazy, which may significantly reduce performance",
            "But may improve machine processing speed","Default: false"})
        public boolean notLazyRecipeModifier = true;

        @Configurable
        @Configurable.Comment({"Random Factor for a recipe not been modified, the bigger the value, the less likely the recipe will be modified.",
            "Default: 5"})
        public int randomFactor = 2;

        @Configurable
        @Configurable.Comment({"Whether to let substation hatches has only normal hatch ability?",
            "This would make normal hatches, substation hatches, and laser hatches compatible with each other.",
            "Default: true"})
        public boolean substationHatchNormalAbility = true;
    }

    public static class RecipeConfigs {
        @Configurable
        @Configurable.Comment({"Is Processing Arrays Have Easier Recipes.",
            "Default: true"})
        public boolean EasierPARecipes = true;

        @Configurable
        @Configurable.Comment({"Do circuit assemblers need higher tier circuits to craft.", "Default: false"})
        public boolean circuitAssemblersNeedHigherTierCircuits = false;

        @Configurable
        @Configurable.Comment({"Do expensive wafers have a better output after cut.", "Default: true"})
        public boolean expensiveWafersHaveHighOutput = true;

        @Configurable
        @Configurable.Comment({"Do easier crystal processor recipes been registered, this will also slightly buff the output of the original recipes.", "Default: true"})
        public boolean easierCrystalProcessors = true;

        @Configurable
        @Configurable.Comment({"Do circuits have higher output, usually doubled.", "Default: true"})
        public boolean circuitsHaveHigherOutput = true;

        @Configurable
        @Configurable.Comment({"Are easier board recipe enabled. This will still need you to complete necessary processing", "Default: true"})
        public boolean easierBoardRecipes = true;

        @Configurable
        @Configurable.Comment({"Do make wetware board cheaper, please disable this if you encountered bug that make the recipe cannot run", "Default: true"})
        public boolean cheaperWetwareBoard = true;

        @Configurable
        @Configurable.Comment({"Are AE2 buses and hatches have easier recipes. This will also add GTRecipes for AE2 components",
            "And will overwrite some GERecipes if applicable.",
            "Default: true"})
        public boolean AE2RecipeSupport = true;

        @Configurable
        @Configurable.Comment({"Are easier recipes for polymers are enabled.", "Default: true"})
        public boolean easierPolymerRecipes = true;

        @Configurable
        @Configurable.Comment({"Are easier recipes for wrought iron and steel are enabled.", "Default: true"})
        public boolean easierWroughtIronAndSteel = true;

        @Configurable
        @Configurable.Comment({"Are easier recipes for cables are enabled.", "Default: true"})
        public boolean easierCableRecipes = true;

        @Configurable
        @Configurable.Comment({"Are ex nihilo like recipes and material being registered and enabled.", "Default: true"})
        public boolean exNihiloLikeRecipesAndMaterials = true;

        @Configurable
        @Configurable.Comment({"Are easier recipes for ore processing are enabled.", "Default: true"})
        public boolean easierOreProcessing = true;

        @Configurable
        @Configurable.Comment({"Are ore yield being strongly buffed?.", "Tends to fix original gregtech ore processing issues.", "Default: true"})
        public boolean buffOreYield = true;

        @Configurable
        @Configurable.Comment({"Do Naquadah Line have overpowered output.", "Default: true"})
        public boolean OPNaquadahLine = true;

        @Configurable
        @Configurable.Comment({"Do certain ingots need EBF to cook.", "Default: true"})
        public boolean ingotNeedEBF = true;

        @Configurable
        @Configurable.Comment({"Do certain hot ingots need Liquid Helium to cool down.", "Default: false"})
        public boolean hotIngotNeedLiquidHelium = false;

        @Configurable
        @Configurable.Comment({"Do molten metal need Vacuum Freezer to cool down.", "Default: false"})
        public boolean moltenCoolDownNeedFreezer = false;

        @Configurable
        @Configurable.Comment({"Autoclave process any gem dust.", "Default: true"})
        public boolean autoclaveProcessAllDust = true;
    }

    public static class MiscConfigs {
        @Configurable
        @Configurable.Comment({"Is fly always enabled. (even in survival mode)", "Currently working not well.", "Default: false"})
        public boolean flyAlwaysEnabled = false;

    }


}
