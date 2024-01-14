package com.yiranmushroom.gtceuao.mixin;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IPlatformEnergyStorage;
import com.gregtechceu.gtceu.api.capability.PlatformEnergyCompat;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.TieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.common.machine.trait.ConverterTrait;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.yiranmushroom.gtceuao.energy.ModEnergyStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

/*
@Mixin(TieredEnergyMachine.class)
public abstract class TieredEnergyMachineMixin extends TieredMachine implements IPlatformEnergyStorage {

    @Unique
    @Persisted
    @DescSynced
    public final NotifiableEnergyContainer energyContainer;

    @Unique
    protected abstract NotifiableEnergyContainer createEnergyContainer(Object... args);

    public TieredEnergyMachineMixin(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier);
        energyContainer = createEnergyContainer(args);

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

    */
/*@Unique
    private ModEnergyStorage FEEnergyStorage = null;

    @Unique
    private int maxForgeEnergyCapacity = -1;

    @Unique
    protected abstract NotifiableEnergyContainer createEnergyContainer(Object... args);

    @Unique
    private NotifiableEnergyContainer energyContainer;

    public TieredEnergyMachineMixin(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier);
        energyContainer = createEnergyContainer(args);
    }

    @Unique
    public ConverterTrait getConverterTrait() {
        return (ConverterTrait)energyContainer;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initializeForgeEnergyStorage(CallbackInfo ci) {
        LOGGER.info("Loading injected TieredEnergyMachine");
        FEEnergyStorage = new ModEnergyStorage(getMaxForgeEnergyCapacity(), getMaxForgeEnergyCapacity()) {
            @Override
            public void onEnergyChanged() {

            }
        };

        if (FEEnergyStorage == null) {
            LOGGER.error("FEEnergyStorage falls to initialize!");
        } else {
            LOGGER.info("FEEnergyStorage initialized! With data below:");
            LOGGER.info(String.valueOf(FEEnergyStorage.getMaxEnergyStored()));
        }
    }


    @Unique
    private int getMaxForgeEnergyCapacity() {
        if (maxForgeEnergyCapacity == -1) {
            var GTEnergyContainer = ((TieredEnergyMachine) (Object) this).energyContainer;
            var result = GTEnergyContainer.getInputVoltage() * GTEnergyContainer.getInputAmperage() * ConfigHolder.INSTANCE.compat.energy.platformToEuRatio;
            if (result > Integer.MAX_VALUE) maxForgeEnergyCapacity = Integer.MAX_VALUE;
            else maxForgeEnergyCapacity = (int) result;
        }

        return maxForgeEnergyCapacity;
    }*//*



}
*/
