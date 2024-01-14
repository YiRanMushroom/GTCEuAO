package com.yiranmushroom.gtceuao.energy;

import net.minecraftforge.energy.EnergyStorage;
import org.spongepowered.asm.mixin.Overwrite;

public abstract class ModEnergyStorage extends EnergyStorage {
    public ModEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = super.extractEnergy(maxExtract, simulate);
        if (energyExtracted > 0) {
            onEnergyChanged();
        }

        return energyExtracted;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return super.receiveEnergy(maxReceive, simulate);
    }

    public abstract void onEnergyChanged();
}
