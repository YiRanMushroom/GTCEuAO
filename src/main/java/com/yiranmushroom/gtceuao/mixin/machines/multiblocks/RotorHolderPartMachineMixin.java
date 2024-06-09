package com.yiranmushroom.gtceuao.mixin.machines.multiblocks;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(RotorHolderPartMachine.class)
public abstract class RotorHolderPartMachineMixin implements IRotorHolderMachine {
    @Shadow(remap = false)
    public abstract int getTierDifference();

    @Override
    public void damageRotor(int damage) {
        if (AOConfigHolder.INSTANCE.machines.disableRotorDamage)
            return;
        else {
            ItemStack stack = this.getRotorStack();
            TurbineRotorBehaviour behavior = TurbineRotorBehaviour.getBehaviour(stack);
            if (behavior != null) {
                behavior.applyRotorDamage(stack, damage);
                this.setRotorStack(stack);
            }
        }
    }

    @Override
    public int getTotalEfficiency() {
        int originalEfficiency = 0;

        int rotorEfficiency = this.getRotorEfficiency();
        if (rotorEfficiency == -1) {
            originalEfficiency = -1;
        } else {
            int holderEfficiency = this.getHolderEfficiency();
            originalEfficiency = holderEfficiency == -1 ? -1 : Math.max(IRotorHolderMachine.getBaseEfficiency(), rotorEfficiency * holderEfficiency / 100);
        }

        return (AOConfigHolder.INSTANCE.machines.buffTurbines ? (int) Math.pow(4.0, getTierDifference() + 1) : 100) * originalEfficiency;
    }
}
