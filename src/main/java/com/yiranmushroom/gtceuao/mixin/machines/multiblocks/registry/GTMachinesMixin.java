package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.GTCEu;
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
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.ProcessingArrayMachineRenderer;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.ProcessingArrayMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifier;
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
import static com.gregtechceu.gtceu.api.registry.GTRegistries.REGISTRATE;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(GTMachines.class)
public abstract class GTMachinesMixin {

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition[] PROCESSING_ARRAY = ConfigHolder.INSTANCE.machines.doProcessingArray ? registerTieredMultis("processing_array", ProcessingArrayMachine::new,
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " Processing Array")
                    .rotationState(RotationState.NON_Y_AXIS)
                    .blockProp(p -> p.noOcclusion().isViewBlocking((state, level, pos) -> false))
                    .shape(Shapes.box(0.001, 0.001, 0.001, 0.999, 0.999, 0.999))
                    .appearanceBlock(() -> ProcessingArrayMachine.getCasingState(tier))
                    .recipeType(DUMMY_RECIPES)
                    .recipeModifier(ProcessingArrayMachine::recipeModifier, true)
                    .pattern(definition -> FactoryBlockPattern.start()
                            .aisle("XXX", "ECE", "EEE")
                            .aisle("XXX", "C#C", "EEE")
                            .aisle("XSX", "ECE", "EEE")
                            .where('S', Predicates.controller(blocks(definition.getBlock())))
                            .where('X', blocks(ProcessingArrayMachine.getCasingState(tier))
                                    .or(Predicates.abilities(PartAbility.IMPORT_ITEMS))
                                    .or(Predicates.abilities(PartAbility.EXPORT_ITEMS))
                                    .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS))
                                    .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS))
                                    .or(Predicates.abilities(PartAbility.INPUT_ENERGY))
                                    .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY))
                                    .or(Predicates.autoAbilities(true, false, false)))
                            .where('C', blocks(CLEANROOM_GLASS.get(), Blocks.GLASS).setMinGlobalLimited(1)
                                    .or(Predicates.abilities(PartAbility.IMPORT_ITEMS))
                                    .or(Predicates.abilities(PartAbility.EXPORT_ITEMS))
                                    .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS))
                                    .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS))
                                    .or(Predicates.abilities(PartAbility.INPUT_ENERGY))
                                    .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY))
                            )
                            .where('E',
                                    Predicates.abilities(PartAbility.IMPORT_ITEMS)
                                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS))
                                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS))
                                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS))
                                            .or(Predicates.abilities(PartAbility.INPUT_ENERGY))
                                            .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY))
                                            .or(blocks(CLEANROOM_GLASS.get(), Blocks.GLASS))
                            )
                            .where('#', air())
                            .build())
                    .tooltips(Component.translatable("gtceu.universal.tooltip.parallel", ProcessingArrayMachine.getMachineLimit(tier)))
                    .renderer(() -> new ProcessingArrayMachineRenderer(tier == IV ?
                            GTCEu.id("block/casings/solid/machine_casing_solid_steel") :
                            GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                            GTCEu.id("block/multiblock/processing_array")))
                    .compassSections(GTCompassSections.TIER[IV])
                    .compassNode("processing_array")
                    .register(),
            IV, LuV) : null;

    @Final
    @Shadow(remap = false)
    public final static MultiblockMachineDefinition ELECTRIC_BLAST_FURNACE = REGISTRATE.multiblock("electric_blast_furnace", CoilWorkableElectricMultiblockMachine::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.BLAST_RECIPES)
            .recipeModifier(AORecipeModifier::ebfOverclock)
            .appearanceBlock(CASING_INVAR_HEATPROOF)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "CCC", "CCC", "XXX")
                    .aisle("XXX", "C#C", "C#C", "XMX")
                    .aisle("XSX", "CCC", "CCC", "XXX")
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('X', blocks(CASING_INVAR_HEATPROOF.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, false)))
                    .where('M', ability(PartAbility.MUFFLER).or(autoAbilities(true, false, false)
                            .or(blocks(CASING_INVAR_HEATPROOF.get()))))
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
                        .where('H', MUFFLER_HATCH[GTValues.LV], Direction.UP)
                        .where('M', MAINTENANCE_HATCH, Direction.NORTH);
                ALL_COILS.entrySet().stream()
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
}
