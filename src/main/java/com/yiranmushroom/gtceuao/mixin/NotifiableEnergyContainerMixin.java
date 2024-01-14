package com.yiranmushroom.gtceuao.mixin;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.IPlatformEnergyStorage;
import com.gregtechceu.gtceu.api.capability.PlatformEnergyCompat;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NotifiableEnergyContainer.class)
public abstract class NotifiableEnergyContainerMixin extends NotifiableRecipeHandlerTrait<Long> implements IEnergyContainer, IPlatformEnergyStorage {

    public NotifiableEnergyContainerMixin(MetaMachine machine) {
        super(machine);
    }

    @Override
    public boolean supportsInsertion() {
        return true;
    }

    @Override
    public boolean supportsExtraction() {
        return true;
    }

    @Override
    public long insert(long maxReceive, boolean simulate) {
        if (maxReceive <= 0) return 0;
        long received = Math.min(getCapacity() - getAmount(), maxReceive);
        received -= received % PlatformEnergyCompat.ratio(true); // avoid rounding issues
        if (!simulate) {
            addEnergy(PlatformEnergyCompat.toEu(received, PlatformEnergyCompat.ratio(true)));
        }
        return received;
    }

    @Override
    public long extract(long maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public long getAmount() {
        return PlatformEnergyCompat.toNativeLong(getEnergyStored(), PlatformEnergyCompat.ratio(true));
    }

    @Override
    public long getCapacity() {
        return PlatformEnergyCompat.toNativeLong(getEnergyCapacity(), PlatformEnergyCompat.ratio(true));
    }
}
