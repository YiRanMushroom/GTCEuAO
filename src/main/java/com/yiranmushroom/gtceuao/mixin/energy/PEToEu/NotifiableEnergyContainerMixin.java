package com.yiranmushroom.gtceuao.mixin.energy.PEToEu;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.IPlatformEnergyStorage;
import com.gregtechceu.gtceu.api.capability.PlatformEnergyCompat;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NotifiableEnergyContainer.class)
public abstract class NotifiableEnergyContainerMixin extends NotifiableRecipeHandlerTrait<Long> implements IEnergyContainer, IPlatformEnergyStorage {

    @Shadow(remap = false)
    protected IO handlerIO;

    @Shadow(remap = false)
    public abstract long getOutputAmperage();

    @Shadow(remap = false)
    public abstract long getOutputVoltage();

    public NotifiableEnergyContainerMixin(MetaMachine machine) {
        super(machine);
    }

    @Override
    public boolean supportsInsertion() {
        return AOConfigHolder.INSTANCE.machines.nativePEToEU && handlerIO == IO.IN || handlerIO == IO.BOTH;
    }

    @Override
    public boolean supportsExtraction() {
        return AOConfigHolder.INSTANCE.machines.nativePEToEU && handlerIO == IO.OUT || handlerIO == IO.BOTH;
    }

    @Override
    public long insert(long maxReceive, boolean simulate) {
//        if (handlerIO == IO.NONE || handlerIO == IO.OUT) return 0;
        if (maxReceive <= 0) return 0;
        long received = Math.min(getCapacity() - getAmount(), Math.min(maxReceive, PlatformEnergyCompat.toNative(getInputAmperage() * getInputVoltage(), PlatformEnergyCompat.ratio(true))));
        received -= received % PlatformEnergyCompat.ratio(true); // avoid rounding issues
        if (!simulate) {
            addEnergy(PlatformEnergyCompat.toEu(received, PlatformEnergyCompat.ratio(true)));
        }
        return received;
    }

    @Override
    public long extract(long maxExtract, boolean simulate) {
//        if (handlerIO == IO.NONE || handlerIO == IO.IN) return 0;
        if (maxExtract <= 0) return 0;
        long sent = Math.min(getAmount(), Math.min(maxExtract, PlatformEnergyCompat.toNative(getOutputAmperage() * getOutputVoltage(), PlatformEnergyCompat.ratio(false))));
        sent -= sent % PlatformEnergyCompat.ratio(true); // avoid rounding issues
        if (!simulate) {
            addEnergy(-PlatformEnergyCompat.toEu(sent, PlatformEnergyCompat.ratio(false)));
        }
        return sent;
    }

    @Override
    public long getAmount() {
        if (!AOConfigHolder.INSTANCE.machines.nativePEToEU)
            return 0;
        return PlatformEnergyCompat.toNativeLong(getEnergyStored(), PlatformEnergyCompat.ratio(true));
    }

    @Override
    public long getCapacity() {
        if (!AOConfigHolder.INSTANCE.machines.nativePEToEU)
            return 0;
        return PlatformEnergyCompat.toNativeLong(getEnergyCapacity(), PlatformEnergyCompat.ratio(true));
    }
}
