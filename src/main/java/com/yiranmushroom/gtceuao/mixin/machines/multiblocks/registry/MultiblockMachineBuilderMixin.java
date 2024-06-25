package com.yiranmushroom.gtceuao.mixin.machines.multiblocks.registry;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.tterrag.registrate.Registrate;
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

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(MultiblockMachineBuilder.class)
public abstract class MultiblockMachineBuilderMixin extends MachineBuilder<MultiblockMachineDefinition> {
    protected MultiblockMachineBuilderMixin(Registrate registrate, String name, Function<ResourceLocation, MultiblockMachineDefinition> definitionFactory, Function<IMachineBlockEntity, MetaMachine> metaMachine, BiFunction<BlockBehaviour.Properties, MultiblockMachineDefinition, IMachineBlock> blockFactory, BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory, TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        super(registrate, name, definitionFactory, metaMachine, blockFactory, itemFactory, blockEntityFactory);
    }

    private static HashMap<String, Boolean> nameMap = new HashMap<>() {
        {
            this.put("electric_blast_furnace", false);
            this.put("iv_processing_array", false);
            this.put("luv_processing_array", false);
            this.put("large_chemical_reactor", false);
            this.put("implosion_compressor", false);
            this.put("pyrolyse_oven", false);
            this.put("cracker", false);
            this.put("distillation_tower", false);
            this.put("vacuum_freezer", false);
            this.put("assembly_line", false);
            this.put("mv_fluid_drilling_rig", false);
            this.put("hv_fluid_drilling_rig", false);
            this.put("ev_fluid_drilling_rig", false);
            this.put("mv_bedrock_ore_miner", false);
            this.put("hv_bedrock_ore_miner", false);
            this.put("ev_bedrock_ore_miner", false);
            this.put("alloy_blast_smelter",false);
        }
    };

    @Inject(method = "register()Lcom/gregtechceu/gtceu/api/machine/MultiblockMachineDefinition;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void newRegister(CallbackInfoReturnable<MultiblockMachineDefinition> cir) {
        if (nameMap.containsKey(name) && !nameMap.get(name)) {
            nameMap.put(name, true);
            cir.setReturnValue(null);
        }
    }

}
