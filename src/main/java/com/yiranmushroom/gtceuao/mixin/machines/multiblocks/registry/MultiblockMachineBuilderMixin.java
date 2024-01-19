package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.registry.registrate.BuilderBase;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.tterrag.registrate.Registrate;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_INVAR_HEATPROOF;

@Mixin(MultiblockMachineBuilder.class)
public abstract class MultiblockMachineBuilderMixin extends MachineBuilder<MultiblockMachineDefinition> {
    protected MultiblockMachineBuilderMixin(Registrate registrate, String name, Function<ResourceLocation, MultiblockMachineDefinition> definitionFactory, Function<IMachineBlockEntity, MetaMachine> metaMachine, BiFunction<BlockBehaviour.Properties, MultiblockMachineDefinition, IMachineBlock> blockFactory, BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory, TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        super(registrate, name, definitionFactory, metaMachine, blockFactory, itemFactory, blockEntityFactory);
    }


    @Inject(method = "register()Lcom/gregtechceu/gtceu/api/machine/MultiblockMachineDefinition;", at = @At("HEAD"), cancellable = false, remap = false)
    private void registerM(CallbackInfoReturnable<MultiblockMachineDefinition> cir) {
        if (AOConfigHolder.INSTANCE.machines.buffMultiblocks)
            switch (this.name) {
                case "electric_blast_furnace" ->
                        ((MultiblockMachineBuilder) (Object) this).recipeModifier(AORecipeModifier::ebfOverclock)
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
                                        .build());
            }
    }

}
