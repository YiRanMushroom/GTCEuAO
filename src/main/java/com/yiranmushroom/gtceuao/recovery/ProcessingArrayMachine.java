package com.yiranmushroom.gtceuao.recovery;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.TieredWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.mojang.datafixers.util.Pair;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifier;
import it.unimi.dsi.fastutil.longs.LongIntPair;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

/**
 * @author KilaBash
 * @date 2023/7/23
 * @implNote ProcessingArrayMachine
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ProcessingArrayMachine extends TieredWorkableElectricMultiblockMachine implements IMachineModifyDrops {


    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ProcessingArrayMachine.class, TieredWorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    @DescSynced
    public final NotifiableItemStackHandler machineStorage;
    //runtime
    @Nullable
    private GTRecipeType[] recipeTypeCache;

    public ProcessingArrayMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
        this.machineStorage = createMachineStorage(args);
    }

    //////////////////////////////////////
    //*****     Initialization    ******//
    //////////////////////////////////////
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    protected NotifiableItemStackHandler createMachineStorage(Object... args) {
        var storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.NONE, slots -> new ItemStackTransfer(1) {
            @Override
            public int getSlotLimit(int slot) {
                return getMachineLimit(getDefinition().getTier());
            }
        });
        storage.setFilter(this::isMachineStack);
        return storage;
    }

    protected boolean isMachineStack(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();

            if (definition instanceof MultiblockMachineDefinition) {
                return false;
            }

            var recipeTypes = definition.getRecipeTypes();
            if (recipeTypes == null) {
                return false;
            }
            for (GTRecipeType type : recipeTypes) {
                if (type != GTRecipeTypes.DUMMY_RECIPES) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    public MachineDefinition getMachineDefinition() {
        if (machineStorage.storage.getStackInSlot(0).getItem() instanceof MetaMachineItem metaMachineItem) {
            return metaMachineItem.getDefinition();
        }
        return null;
    }

    @Override
    @NotNull
    public GTRecipeType[] getRecipeTypes() {
        if (recipeTypeCache == null) {
            var definition = getMachineDefinition();
            recipeTypeCache = definition == null ? null : definition.getRecipeTypes();
        }
        if (recipeTypeCache == null) {
            recipeTypeCache = new GTRecipeType[]{GTRecipeTypes.DUMMY_RECIPES};
        }
        return recipeTypeCache;
    }

    @NotNull
    @Override
    public GTRecipeType getRecipeType() {
        return getRecipeTypes()[getActiveRecipeType()];
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            machineStorage.addChangedListener(this::onMachineChanged);
        }
    }

    protected void onMachineChanged() {
        recipeTypeCache = null;
        if (isFormed) {
            if (getRecipeLogic().getLastRecipe() != null) {
                getRecipeLogic().markLastRecipeDirty();
            }
            getRecipeLogic().updateTickSubscription();
        }
    }

    @Override
    public void onDrops(List<ItemStack> drops, Player entity) {
        clearInventory(drops, machineStorage.storage);
    }

    //////////////////////////////////////
    //*******    Recipe Logic    *******//
    //////////////////////////////////////

    /**
     * For available recipe tier, decided by the held machine.
     */
    @Override
    public int getTier() {
        if (AOConfigHolder.INSTANCE.machines.PAIgnoreTier) {
            return GTUtil.getFloorTierByVoltage(this.getMaxVoltage());
        }
        var definition = getMachineDefinition();
        return definition == null ? 0 : definition.getTier();
    }

    @Override
    public long getMaxVoltage() {
        return this.getEnergyContainer().getInputVoltage();
        /*if (this.energyContainer == null) {
            this.energyContainer = this.getEnergyContainer();
        }
        long highestVoltage;

        highestVoltage = this.energyContainer.getHighestInputVoltage();

        if (this.energyContainer.getNumHighestInputContainers() > 1) {
            int tier = GTUtil.getTierByVoltage(highestVoltage);
            return GTValues.V[Math.min(tier + 1, 14)];
        } else {
            return highestVoltage;
        }*/
    }

    @Override
    public int getOverclockTier() {
        if (AOConfigHolder.INSTANCE.machines.PAIgnoreTier) {
            return GTUtil.getFloorTierByVoltage(this.getMaxVoltage());
        }
        MachineDefinition machineDefinition = getMachineDefinition();
        int machineTier = machineDefinition == null ? getDefinition().getTier() : Math.min(getDefinition().getTier(), machineDefinition.getTier());
        return Math.min(machineTier, GTUtil.getTierByVoltage(getMaxVoltage()));
    }

    @Override
    public int getMinOverclockTier() {
        return getOverclockTier();
    }

    @Override
    public int getMaxOverclockTier() {
        return getOverclockTier();
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipeOriginal) {
        if (machine instanceof ProcessingArrayMachine processingArray && processingArray.machineStorage.storage.getStackInSlot(0).getCount() > 0) {

            GTRecipe recipe = recipeOriginal.copy();

            if (RecipeHelper.getRecipeEUtTier(recipe) > processingArray.getTier())
                return null;

            if (AOConfigHolder.INSTANCE.machines.PAHasOPChance) {
                for (var content : recipe.getOutputContents(ItemRecipeCapability.CAP)) {
                    content.chance = 1.0F;
                }
            }

            return AORecipeModifier.prefectSubtickParallel(processingArray,recipe,false, (it) ->
                ((ProcessingArrayMachine)it).machineStorage.storage.getStackInSlot(0).getCount() *
                AOConfigHolder.INSTANCE.machines.ParallelMultiplier);
        }
        return null;
    }

//    @Override
//    public Map<RecipeCapability<?>, Integer> getOutputLimits() {
//        if (getMachineDefinition() != null) {
//            return getMachineDefinition().getRecipeOutputLimits();
//        }
//        return GTRegistries.RECIPE_CAPABILITIES.values().stream().map(key -> Map.entry(key, 0)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }

    //////////////////////////////////////
    //********        Gui       ********//
    //////////////////////////////////////

    @Override
    public void addDisplayText(List<Component> textList) {
        textList.add(Component.translatable("gtceu.universal.tooltip.deprecated"));
        super.addDisplayText(textList);
        if (isActive()) {
            textList.add(Component.translatable("gtceu.machine.machine_hatch.locked").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        }
    }

    @Override
    public Widget createUIWidget() {
        var widget = super.createUIWidget();
        if (widget instanceof WidgetGroup group) {
            var size = group.getSize();
            group.addWidget(new SlotWidget(machineStorage.storage, 0, size.width - 30, size.height - 30, true, true)
                .setBackground(GuiTextures.SLOT));
        }
        return widget;
    }

    //////////////////////////////////////
    //********     Structure    ********//
    //////////////////////////////////////
    public static Block getCasingState(int tier) {
        if (tier <= GTValues.IV) {
            return GTBlocks.CASING_STEEL_SOLID.get();
        } else {
            return GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get();
        }
    }

    public static int getMachineLimit(Integer tier) {
        return tier <= GTValues.IV ? 16 : 64;
    }
}
