package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.ProcessingArrayMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.ProcessingArrayMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.registry.GTRegistries.REGISTRATE;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CLEANROOM_GLASS;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;

@Mixin(GTMachines.class)
public abstract class GTMachinesMixin {

    /**
     * @author YiranMushroom
     * @reason Cannot Use Head Injection
     */
    @Inject(method = "registerTieredMultis", at = @At("HEAD"), cancellable = true, remap = false)
    private static void registerTieredMultis(String name, BiFunction<IMachineBlockEntity, Integer, MultiblockControllerMachine> factory, BiFunction<Integer, MultiblockMachineBuilder, MultiblockMachineDefinition> builder, int[] tiers, CallbackInfoReturnable<MultiblockMachineDefinition[]> cir) {

        // Processing Array
        if (name.equals("processing_array")) {
            cir.setReturnValue(gtceuao$originalRegisterTieredMultis(name, factory, (tier, p_builder) -> p_builder
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
                            .where('#', Predicates.air())
                            .build())
                    .tooltips(Component.translatable("gtceu.universal.tooltip.parallel", ProcessingArrayMachine.getMachineLimit(tier)))
                    .renderer(() -> new ProcessingArrayMachineRenderer(tier == IV ?
                            GTCEu.id("block/casings/solid/machine_casing_solid_steel") :
                            GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                            GTCEu.id("block/multiblock/processing_array")))
                    .compassSections(GTCompassSections.TIER[IV])
                    .compassNode("processing_array")
                    .register(), tiers)
            );
        }

    }

    @Unique
    private static MultiblockMachineDefinition[] gtceuao$originalRegisterTieredMultis(String name,
                                                                                      BiFunction<IMachineBlockEntity, Integer, MultiblockControllerMachine> factory,
                                                                                      BiFunction<Integer, MultiblockMachineBuilder, MultiblockMachineDefinition> builder,
                                                                                      int... tiers) {
        MultiblockMachineDefinition[] definitions = new MultiblockMachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = REGISTRATE.multiblock(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name, holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

}
