package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.AssemblyLineMachine;
import com.yiranmushroom.gtceuao.machines.AOMachines;
import com.yiranmushroom.gtceuao.recovery.ProcessingArrayMachine;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.BedrockOreMinerMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FluidDrillMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.Platform;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.GTValues.ALL_TIERS;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

@Mixin(GTMachines.class)
public abstract class GTMachinesMixin {
    @Final
    @Shadow(remap = false)
    public static final MultiblockMachineDefinition[] FLUID_DRILLING_RIG = registerTieredMultis("fluid_drilling_rig", FluidDrillMachine::new, (tier, builder) -> builder
            .rotationState(RotationState.NON_Y_AXIS)
            .langValue("%s Fluid Drilling Rig %s".formatted(VLVH[tier], VLVT[tier]))
            .recipeType(DUMMY_RECIPES)
            .tooltips(
                Component.translatable("gtceu.machine.fluid_drilling_rig.description"),
                Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", FormattingUtil.formatNumbers(gtceuao$getDepletionChanceFluid(tier) * 100)),
                Component.translatable("gtceu.universal.tooltip.energy_tier_range", GTValues.VNF[tier], GTValues.VNF[tier + 1]),
                Component.translatable("gtceu.machine.fluid_drilling_rig.production", FluidDrillMachine.getRigMultiplier(tier), FormattingUtil.formatNumbers(FluidDrillMachine.getRigMultiplier(tier) * 1.5)))
            .appearanceBlock(() -> FluidDrillMachine.getCasingState(tier))
            .pattern((definition) -> FactoryBlockPattern.start()
                .aisle("XXX", "#F#", "#F#", "#F#", "###", "###", "###")
                .aisle("XXX", "FCF", "FCF", "FCF", "#F#", "#F#", "#F#")
                .aisle("XSX", "#F#", "#F#", "#F#", "###", "###", "###")
                .where('S', controller(blocks(definition.get())))
                .where('X', blocks(FluidDrillMachine.getCasingState(tier)).setMinGlobalLimited(3)
                    .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(3))
                    .or(abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                .where('C', blocks(FluidDrillMachine.getCasingState(tier)))
                .where('F', blocks(FluidDrillMachine.getFrameState(tier)))
                .where('#', any())
                .build())
            .workableCasingRenderer(FluidDrillMachine.getBaseTexture(tier), GTCEu.id("block/multiblock/fluid_drilling_rig"), false)
            .compassSections(GTCompassSections.TIER[MV])
            .compassNode("fluid_drilling_rig")
            .register(),
        MV, HV, EV);

    @Final
    @Shadow(remap = false)
    public static final MultiblockMachineDefinition ASSEMBLY_LINE = REGISTRATE
        .multiblock("assembly_line", AssemblyLineMachine::new)
        .rotationState(RotationState.ALL)
        .recipeType(GTRecipeTypes.ASSEMBLY_LINE_RECIPES)
        .alwaysTryModifyRecipe(true)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL,GTRecipeModifiers.DEFAULT_ENVIRONMENT_REQUIREMENT,
            AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::perfectCoilMachineParallel)
        .appearanceBlock(CASING_STEEL_SOLID)
        .pattern(definition -> FactoryBlockPattern.start(BACK, UP, RIGHT)
            .aisle("FIF", "RTR", "SAG", "#Y#")
            .aisle("FIF", "RTR", "DAG", "#Y#").setRepeatable(3, 15)
            .aisle("FOF", "RTR", "DAG", "#Y#")
            .where('S', Predicates.controller(blocks(definition.getBlock())))
            .where('F', blocks(CASING_STEEL_SOLID.get())
            .or(ConfigHolder.INSTANCE.machines.orderedAssemblyLineFluids ?
                Predicates.abilities(PartAbility.IMPORT_FLUIDS) :
                Predicates.abilities(PartAbility.IMPORT_FLUIDS_1X).setMaxGlobalLimited(4)))
            .where('O',
                Predicates.abilities(PartAbility.EXPORT_ITEMS)
                    .addTooltips(Component.translatable("gtceu.multiblock.pattern.location_end")))
            .where('Y',
                blocks(CASING_STEEL_SOLID.get()).or(Predicates.abilities(PartAbility.INPUT_ENERGY)
                    .setMinGlobalLimited(1).setMaxGlobalLimited(2)))
            .where('I', blocks(ITEM_IMPORT_BUS[0].getBlock()))
            .where('G', blocks(CASING_GRATE.get()))
            .where('A', blocks(CASING_ASSEMBLY_CONTROL.get()))
            .where('R', blocks(CASING_LAMINATED_GLASS.get()))
            .where('T', blocks(CASING_ASSEMBLY_LINE.get()))
            .where('D', dataHatchPredicate(blocks(CASING_GRATE.get())))
            .where('#', Predicates.any())
            .build())
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
            GTCEu.id("block/multiblock/assembly_line"), false)
        .compassSections(GTCompassSections.TIER[IV])
        .compassNodeSelf()
        .register();

    @Shadow(remap = false)
    private static TraceabilityPredicate dataHatchPredicate(TraceabilityPredicate def) {
        return ConfigHolder.INSTANCE.machines.enableResearch ? Predicates.abilities(new PartAbility[]{PartAbility.DATA_ACCESS, PartAbility.OPTICAL_DATA_RECEPTION}).setExactLimit(1).or(def) : def;
    }

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition VACUUM_FREEZER = REGISTRATE.multiblock("vacuum_freezer", WorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.ALL)
        .recipeType(GTRecipeTypes.VACUUM_RECIPES)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::perfectCoilMachineParallel)
        .appearanceBlock(CASING_ALUMINIUM_FROSTPROOF)
        .pattern(definition -> FactoryBlockPattern.start()
            .aisle("XXX", "XXX", "XXX")
            .aisle("XXX", "X#X", "XXX")
            .aisle("XXX", "XSX", "XXX")
            .where('S', Predicates.controller(blocks(definition.getBlock())))
            .where('X', blocks(CASING_ALUMINIUM_FROSTPROOF.get())
                .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                .or(Predicates.autoAbilities(true, false, false).or(Predicates.abilities(PartAbility.MUFFLER))))
            .where('#', Predicates.air())
            .build())
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
            GTCEu.id("block/multiblock/vacuum_freezer"), false)
        .compassSections(GTCompassSections.TIER[HV])
        .compassNodeSelf()
        .register();

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition DISTILLATION_TOWER = REGISTRATE.multiblock("distillation_tower", CoilWorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.NON_Y_AXIS)
        .recipeType(GTRecipeTypes.DISTILLATION_RECIPES)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::perfectCoilMachineParallel)
        .appearanceBlock(CASING_STAINLESS_CLEAN)
        .pattern(definition -> FactoryBlockPattern.start(RIGHT, BACK, UP)
            .aisle("YSY", "YMY", "YYY")
            .aisle("XXX", "X#X", "XXX").setRepeatable(1, 11)
            .aisle("XXX", "XXX", "XXX")
            .where('S', Predicates.controller(blocks(definition.getBlock())))
            .where('Y', blocks(CASING_STAINLESS_CLEAN.get())
                .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(1))
                .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1))
                .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS))
                .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS)))
            .where('X', blocks(CASING_STAINLESS_CLEAN.get())
                .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS)))
            .where('M', blocks(CASING_STAINLESS_CLEAN.get())
                .or(Predicates.heatingCoils()))
            .where('#', Predicates.air())
            .build())
        .partSorter(Comparator.comparingInt(a -> a.self().getPos().getY()))
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
            GTCEu.id("block/multiblock/distillation_tower"), false)
        .compassSections(GTCompassSections.TIER[EV])
        .compassNodeSelf()
        .register();

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition CRACKER = REGISTRATE.multiblock("cracker", CoilWorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.ALL)
        .recipeType(GTRecipeTypes.CRACKING_RECIPES)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::crackerOverclock)
        .appearanceBlock(CASING_STAINLESS_CLEAN)
        .pattern(definition -> FactoryBlockPattern.start()
            .aisle("HCHCH", "HCHCH", "HCHCH")
            .aisle("HCHCH", "H###H", "HCHCH")
            .aisle("HCHCH", "HCOCH", "HCHCH")
            .where('O', Predicates.controller(blocks(definition.get())))
            .where('H', blocks(CASING_STAINLESS_CLEAN.get())
                .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                .or(Predicates.autoAbilities(true, false, false).or(Predicates.abilities(PartAbility.MUFFLER))))
            .where('#', Predicates.air())
            .where('C', Predicates.heatingCoils())
            .build())
        .shapeInfos(definition -> {
            List<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            var builder = MultiblockShapeInfo.builder()
                .aisle("FCICD", "HCSCH", "HCMCH")
                .aisle("ECHCH", "H###H", "HCHCH")
                .aisle("ECHCH", "HCHCH", "HCHCH")
                .where('S', definition, Direction.NORTH)
                .where('H', CASING_STAINLESS_CLEAN.getDefaultState())
                .where('E', ENERGY_INPUT_HATCH[GTValues.LV], Direction.WEST)
                .where('I', ITEM_IMPORT_BUS[GTValues.LV], Direction.NORTH)
                .where('F', FLUID_IMPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('D', FLUID_EXPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('M', MAINTENANCE_HATCH, Direction.NORTH)
                .where('#', Blocks.AIR.defaultBlockState());
            GTCEuAPI.HEATING_COILS.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getTier()))
                .forEach(coil -> shapeInfo.add(builder.shallowCopy().where('C', coil.getValue().get()).build()));
            return shapeInfo;
        })
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
            GTCEu.id("block/multiblock/cracking_unit"), false)
        .tooltips(Component.translatable("gtceu.machine.cracker.tooltip.1"))
        .additionalDisplay((controller, components) -> {
            if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                components.add(Component.translatable("gtceu.multiblock.cracking_unit.energy", 100 - 10 * coilMachine.getCoilTier()));
            }
        })
        .compassSections(GTCompassSections.TIER[EV])
        .compassNodeSelf()
        .register();

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition PYROLYSE_OVEN = REGISTRATE.multiblock("pyrolyse_oven", CoilWorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.ALL)
        .recipeType(GTRecipeTypes.PYROLYSE_RECIPES)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::pyrolyseOvenOverclock)
        .appearanceBlock(MACHINE_CASING_ULV)
        .pattern(definition -> FactoryBlockPattern.start()
            .aisle("XXX", "XXX", "XXX")
            .aisle("CCC", "C#C", "CCC")
            .aisle("CCC", "C#C", "CCC")
            .aisle("XXX", "XSX", "XXX")
            .where('S', Predicates.controller(blocks(definition.get())))
            .where('X', blocks(MACHINE_CASING_ULV.get()).or(Predicates.autoAbilities(definition.getRecipeTypes()))
                .or(Predicates.autoAbilities(true, false, false).or(Predicates.abilities(PartAbility.MUFFLER))))
            .where('C', Predicates.heatingCoils())
            .where('#', Predicates.air())
            .build())
        .shapeInfos(definition -> {
            List<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            var builder = MultiblockShapeInfo.builder()
                .aisle("IXO", "XSX", "FMD")
                .aisle("CCC", "C#C", "CCC")
                .aisle("CCC", "C#C", "CCC")
                .aisle("EEX", "XHX", "XXX")
                .where('S', definition, Direction.NORTH)
                .where('X', MACHINE_CASING_ULV.getDefaultState())
                .where('E', ENERGY_INPUT_HATCH[GTValues.LV], Direction.SOUTH)
                .where('I', ITEM_IMPORT_BUS[GTValues.LV], Direction.NORTH)
                .where('O', ITEM_EXPORT_BUS[GTValues.LV], Direction.NORTH)
                .where('F', FLUID_IMPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('D', FLUID_EXPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('H', GTMachines.MUFFLER_HATCH[GTValues.LV], Direction.SOUTH)
                .where('M', MAINTENANCE_HATCH, Direction.NORTH)
                .where('#', Blocks.AIR.defaultBlockState());
            GTCEuAPI.HEATING_COILS.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getTier()))
                .forEach(coil -> shapeInfo.add(builder.shallowCopy().where('C', coil.getValue().get()).build()));
            return shapeInfo;
        })
        .workableCasingRenderer(GTCEu.id("block/casings/voltage/ulv/side"),
            GTCEu.id("block/multiblock/pyrolyse_oven"), false)
        .tooltips(Component.translatable("gtceu.machine.pyrolyse_oven.tooltip.1"))
        .additionalDisplay((controller, components) -> {
            if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                components.add(Component.translatable("gtceu.multiblock.pyrolyse_oven.speed", coilMachine.getCoilTier() == 0 ? 75 : 50 * (coilMachine.getCoilTier() + 1)));
            }
        })
        .compassSections(GTCompassSections.TIER[MV])
        .compassNodeSelf()
        .register();
    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition IMPLOSION_COMPRESSOR = REGISTRATE.multiblock("implosion_compressor", WorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.ALL)
        .recipeType(GTRecipeTypes.IMPLOSION_RECIPES)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::perfectCoilMachineParallel)
        .appearanceBlock(CASING_STEEL_SOLID)
        .pattern(definition -> FactoryBlockPattern.start()
            .aisle("XXX", "XXX", "XXX")
            .aisle("XXX", "X#X", "XXX")
            .aisle("XXX", "XSX", "XXX")
            .where('S', controller(blocks(definition.get())))
            .where('X', blocks(CASING_STEEL_SOLID.get())
                .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                .or(Predicates.autoAbilities(true, true, false)))
            .where('#', Predicates.air())
            .build())
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
            GTCEu.id("block/multiblock/implosion_compressor"), false)
        .compassSections(GTCompassSections.TIER[HV])
        .compassNodeSelf()
        .register();

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition LARGE_CHEMICAL_REACTOR = REGISTRATE.multiblock("large_chemical_reactor", CoilWorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.ALL)
        .recipeType(GTRecipeTypes.LARGE_CHEMICAL_RECIPES)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::perfectCoilMachineParallel)
        .appearanceBlock(CASING_PTFE_INERT)
        .pattern(definition -> {
            var casing = blocks(CASING_PTFE_INERT.get());
            var abilities = Predicates.autoAbilities(definition.getRecipeTypes())
                .or(Predicates.autoAbilities(true, false, false));
            return FactoryBlockPattern.start()
                .aisle("XXX", "XCX", "XXX")
                .aisle("XCX", "CPC", "XCX")
                .aisle("XXX", "XSX", "XXX")
                .where('S', Predicates.controller(blocks(definition.getBlock())))
                .where('X', casing.or(abilities))
                .where('P', blocks(CASING_POLYTETRAFLUOROETHYLENE_PIPE.get()))
                .where('C', heatingCoils().setExactLimit(1)
                    .or(abilities)
                    .or(casing))
                .build();
        })
        .shapeInfos(definition -> {
            ArrayList<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            var baseBuilder = MultiblockShapeInfo.builder()
                .where('S', definition, Direction.NORTH)
                .where('X', CASING_PTFE_INERT.getDefaultState())
                .where('P', CASING_POLYTETRAFLUOROETHYLENE_PIPE.getDefaultState())
                .where('C', COIL_CUPRONICKEL.getDefaultState())
                .where('I', ITEM_IMPORT_BUS[3], Direction.NORTH)
                .where('E', ENERGY_INPUT_HATCH[3], Direction.NORTH)
                .where('O', ITEM_EXPORT_BUS[3], Direction.NORTH)
                .where('F', FLUID_IMPORT_HATCH[3], Direction.NORTH)
                .where('M', MAINTENANCE_HATCH, Direction.NORTH)
                .where('H', FLUID_EXPORT_HATCH[3], Direction.NORTH);
            shapeInfo.add(baseBuilder.shallowCopy()
                .aisle("IXO", "FSH", "XMX")
                .aisle("XXX", "XPX", "XXX")
                .aisle("XEX", "XCX", "XXX")
                .build()
            );
            shapeInfo.add(baseBuilder.shallowCopy()
                .aisle("IXO", "FSH", "XMX")
                .aisle("XXX", "XPX", "XCX")
                .aisle("XEX", "XXX", "XXX")
                .build()
            );
            shapeInfo.add(baseBuilder.shallowCopy()
                .aisle("IXO", "FSH", "XMX")
                .aisle("XCX", "XPX", "XXX")
                .aisle("XEX", "XXX", "XXX")
                .build()
            );
            shapeInfo.add(baseBuilder.shallowCopy()
                .aisle("IXO", "FSH", "XMX")
                .aisle("XXX", "CPX", "XXX")
                .aisle("XEX", "XXX", "XXX")
                .build()
            );
            shapeInfo.add(baseBuilder.shallowCopy()
                .aisle("IXO", "FSH", "XMX")
                .aisle("XXX", "XPC", "XXX")
                .aisle("XEX", "XXX", "XXX")
                .build()
            );
            return shapeInfo;
        })
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
            GTCEu.id("block/multiblock/large_chemical_reactor"), false)
        .compassSections(GTCompassSections.TIER[HV])
        .compassNodeSelf()
        .register();
    @Final
    @Shadow(remap = false)
    public static MultiblockMachineDefinition[] PROCESSING_ARRAY = registerTieredMultis("processing_array", ProcessingArrayMachine::new,
        (tier, builder) -> builder
            .langValue(VNF[tier] + " Processing Array")
            .rotationState(RotationState.ALL)
            .blockProp(p -> p.noOcclusion().isViewBlocking((state, level, pos) -> false))
            .shape(Shapes.box(0.001, 0.001, 0.001, 0.999, 0.999, 0.999))
            .appearanceBlock(() -> ProcessingArrayMachine.getCasingState(tier))
            .recipeType(DUMMY_RECIPES)
            .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL,ProcessingArrayMachine::recipeModifier)
            .pattern(definition -> FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX")
                .aisle("XXX", "X#X", "XXX")
                .aisle("XSX", "XXX", "XXX")
                .where('S', Predicates.controller(blocks(definition.getBlock())))
                .where('X', blocks(ProcessingArrayMachine.getCasingState(tier))
                    .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                    .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                    .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                    .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                    .or(Predicates.abilities(PartAbility.INPUT_ENERGY).setPreviewCount(1))
                    .or(Predicates.autoAbilities(true, false, false))
                    .or(blocks(CLEANROOM_GLASS.get(), Blocks.GLASS)))
                .where('#', air())
                .build())
            .tooltips(Component.translatable("gtceu.universal.tooltip.parallel", ProcessingArrayMachine.getMachineLimit(tier)))
            .workableCasingRenderer(tier == IV ?
                    GTCEu.id("block/casings/solid/machine_casing_solid_steel") :
                    GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                GTCEu.id("block/multiblock/processing_array"))
            .compassSections(GTCompassSections.TIER[IV])
            .compassNode("processing_array")
            .register(),
        IV, LuV);

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition ELECTRIC_BLAST_FURNACE = REGISTRATE.multiblock("electric_blast_furnace", CoilWorkableElectricMultiblockMachine::new)
        .rotationState(RotationState.ALL)
        .recipeType(GTRecipeTypes.BLAST_RECIPES)
        .recipeModifiers(AORecipeModifiers.PERFECT_SUBTICK_PARALLEL, AORecipeModifiers::ebfOverclock)
        .appearanceBlock(CASING_INVAR_HEATPROOF)
        .pattern(definition -> FactoryBlockPattern.start()
            .aisle("XXX", "CCC", "CCC", "XXX")
            .aisle("XXX", "C#C", "C#C", "XMX")
            .aisle("XSX", "CCC", "CCC", "XXX")
            .where('S', controller(blocks(definition.getBlock())))
            .where('X', blocks(CASING_INVAR_HEATPROOF.get())
                .or(autoAbilities(definition.getRecipeTypes()))
                .or(autoAbilities(true, false, false)))
            .where('M',
                Predicates.abilities(PartAbility.MUFFLER)
            )
            .where('C', heatingCoils())
            .where('#', air())
            .build())
        .shapeInfos(definition -> {
            List<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            var builder = MultiblockShapeInfo.builder()
                .aisle("ISO", "CCC", "CCC", "XMX")
                .aisle("FXD", "C#C", "C#C", "XHX")
                .aisle("EEX", "CCC", "CCC", "XXX")
                .where('X', CASING_INVAR_HEATPROOF.getDefaultState())
                .where('S', definition, Direction.NORTH)
                .where('#', Blocks.AIR.defaultBlockState())
                .where('E', ENERGY_INPUT_HATCH[GTValues.LV], Direction.SOUTH)
                .where('I', ITEM_IMPORT_BUS[GTValues.LV], Direction.NORTH)
                .where('O', ITEM_EXPORT_BUS[GTValues.LV], Direction.NORTH)
                .where('F', FLUID_IMPORT_HATCH[GTValues.LV], Direction.WEST)
                .where('D', FLUID_EXPORT_HATCH[GTValues.LV], Direction.EAST)
                .where('H', GTMachines.MUFFLER_HATCH[GTValues.LV], Direction.UP)
                .where('M', MAINTENANCE_HATCH, Direction.NORTH);
            GTCEuAPI.HEATING_COILS.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getTier()))
                .forEach(coil -> shapeInfo.add(builder.shallowCopy().where('C', coil.getValue().get()).build()));
            return shapeInfo;
        })
        .recoveryItems(() -> new ItemLike[]{GTItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get()})
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_heatproof"),
            GTCEu.id("block/multiblock/electric_blast_furnace"), false)
        .tooltips(Component.translatable("gtceu.machine.electric_blast_furnace.tooltip.1",
            Component.translatable("gtceu.machine.electric_blast_furnace.tooltip.2"),
            Component.translatable("gtceu.machine.electric_blast_furnace.tooltip.3")))
        .additionalDisplay((controller, components) -> {
            if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                components.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature",
                    Component.translatable(FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature() + 100L * Math.max(0, coilMachine.getTier() - GTValues.MV)) + "K").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
            }
        })
        .compassSections(GTCompassSections.TIER[MV])
        .compassNodeSelf()
        .register();

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/GTCEu;isKubeJSLoaded()Z"), remap = false)
    private static void init(CallbackInfo ci) {
        LOGGER.info("GTCEU-AO: Registering Multiblocks");

        AOMachines.init();

        if ((ConfigHolder.INSTANCE.machines.doBedrockOres || Platform.isDevEnv()))
            BEDROCK_ORE_MINER = registerTieredMultis("bedrock_ore_miner", BedrockOreMinerMachine::new, (tier, builder) -> builder
                    .rotationState(RotationState.NON_Y_AXIS)
                    .langValue("%s Bedrock Ore Miner %s".formatted(VLVH[tier], VLVT[tier]))
                    .recipeType(new GTRecipeType(GTCEu.id("bedrock_ore_miner"), "dummy"))
                    .tooltips(
                        Component.translatable("gtceu.machine.bedrock_ore_miner.description"),
                        Component.translatable("gtceu.machine.bedrock_ore_miner.depletion", FormattingUtil.formatNumbers(gtceuao$getDepletionChanceItem(tier))),
                        Component.translatable("gtceu.universal.tooltip.energy_tier_range", GTValues.VNF[tier], GTValues.VNF[tier + 1]),
                        Component.translatable("gtceu.machine.bedrock_ore_miner.production", BedrockOreMinerMachine.getRigMultiplier(tier), FormattingUtil.formatNumbers(BedrockOreMinerMachine.getRigMultiplier(tier) * 1.5)))
                    .appearanceBlock(() -> BedrockOreMinerMachine.getCasingState(tier))
                    .pattern((definition) -> FactoryBlockPattern.start()
                        .aisle("XXX", "#F#", "#F#", "#F#", "###", "###", "###")
                        .aisle("XXX", "FCF", "FCF", "FCF", "#F#", "#F#", "#F#")
                        .aisle("XSX", "#F#", "#F#", "#F#", "###", "###", "###")
                        .where('S', controller(blocks(definition.get())))
                        .where('X', blocks(BedrockOreMinerMachine.getCasingState(tier)).setMinGlobalLimited(3)
                            .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(3))
                            .or(abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(1)))
                        .where('C', blocks(BedrockOreMinerMachine.getCasingState(tier)))
                        .where('F', blocks(BedrockOreMinerMachine.getFrameState(tier)))
                        .where('#', any())
                        .build())
                    .workableCasingRenderer(BedrockOreMinerMachine.getBaseTexture(tier), GTCEu.id("block/multiblock/bedrock_ore_miner"), false)
                    .register(),
                MV, HV, EV);

    }

    @Shadow(remap = false)
    public static MultiblockMachineDefinition[] registerTieredMultis(String name,
                                                                     BiFunction<IMachineBlockEntity, Integer, MultiblockControllerMachine> factory,
                                                                     BiFunction<Integer, MultiblockMachineBuilder, MultiblockMachineDefinition> builder,
                                                                     int... tiers) {
        return null;
    }

    @Unique
    private static MachineDefinition[] gtceuao$registerTieredMachines(String name,
                                                                      BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                                                      BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
                                                                      int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = REGISTRATE.machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name, holder -> factory.apply(holder, tier))
                .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    @Inject(method = "registerTieredMachines", at = @At("HEAD"), cancellable = true, remap = false)
    private static void registerTieredMachines(String name,
                                               BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                               BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
                                               int[] tiers, CallbackInfoReturnable<MachineDefinition[]> cir) {
        switch (name) {

            case "input_bus" -> cir.setReturnValue(
                gtceuao$registerTieredMachines("input_bus",
                    (holder, tier) -> new ItemBusPartMachine(holder, tier, IO.IN),
                    (tier, p_builder) -> p_builder
                        .langValue(VNF[tier] + " Input Bus")
                        .rotationState(RotationState.ALL)
                        .abilities(tier == 0 ? new PartAbility[]{PartAbility.IMPORT_ITEMS, PartAbility.STEAM_IMPORT_ITEMS} : new PartAbility[]{PartAbility.IMPORT_ITEMS})
                        .overlayTieredHullRenderer("item_bus.import")
                        .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                            Component.translatable("gtceu.universal.tooltip.item_storage_capacity", Math.min(100, (1 + Math.min(9, tier) * (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches ? 2 : 1)) * (1 + Math.min(9, tier) * (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches ? 2 : 1)))))
                        .compassNode("item_bus")
                        .register(),
                    ALL_TIERS)
            );

            case "output_bus" -> cir.setReturnValue(
                gtceuao$registerTieredMachines("output_bus",
                    (holder, tier) -> new ItemBusPartMachine(holder, tier, IO.OUT),
                    (tier, p_builder) -> p_builder
                        .langValue(VNF[tier] + " Output Bus")
                        .rotationState(RotationState.ALL)
                        .abilities(tier == 0 ? new PartAbility[]{PartAbility.EXPORT_ITEMS, PartAbility.STEAM_EXPORT_ITEMS} : new PartAbility[]{PartAbility.EXPORT_ITEMS})
                        .overlayTieredHullRenderer("item_bus.export")
                        .tooltips(Component.translatable("gtceu.machine.item_bus.export.tooltip"),
                            Component.translatable("gtceu.universal.tooltip.item_storage_capacity", Math.min(100, (1 + Math.min(9, tier) * (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches ? 2 : 1)) * (1 + Math.min(9, tier) * (AOConfigHolder.INSTANCE.machines.buffBusesAndHatches ? 2 : 1)))))
                        .compassNode("item_bus")
                        .register(),
                    ALL_TIERS)
            );
        }
    }

    @Unique
    private static double gtceuao$getDepletionChanceItem(int tier) {
        if (AOConfigHolder.INSTANCE.machines.bedrockDrillsDepleteResources)
            return 100.0 / BedrockOreMinerMachine.getDepletionChance(tier);
        return 0.0;
    }

    @Unique
    private static double gtceuao$getDepletionChanceFluid(int tier) {
        if (AOConfigHolder.INSTANCE.machines.bedrockDrillsDepleteResources)
            return 100.0 / FluidDrillMachine.getDepletionChance(tier);
        return 0.0;
    }
}
