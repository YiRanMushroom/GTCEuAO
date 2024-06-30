package com.yiranmushroom.gtceuao.mixin.machines.logic;

import com.gregtechceu.gtceu.api.capability.IWorkable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.lowdragmc.lowdraglib.syncdata.IEnhancedManaged;
import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.Random;
import java.util.random.RandomGenerator;

@Mixin(RecipeLogic.class)
public abstract class RecipeLogicMixin extends MachineTrait implements IEnhancedManaged, IWorkable, IFancyTooltip {
    @Shadow(remap = false)
    @Final
    public IRecipeLogicMachine machine;

    @Shadow(remap = false)
    @Nullable
    protected GTRecipe lastRecipe;

    @Shadow(remap = false)
    protected abstract boolean handleRecipeIO(GTRecipe recipe, IO io);

    @Shadow(remap = false)
    @Nullable
    protected GTRecipe lastOriginRecipe;

    @Shadow(remap = false)
    public abstract void markLastRecipeDirty();

    @Shadow(remap = false)
    protected boolean recipeDirty;

    @Shadow(remap = false)
    public abstract void setupRecipe(GTRecipe recipe);

    @Shadow(remap = false)
    public abstract void setStatus(RecipeLogic.Status status);

    @Shadow(remap = false)
    protected int progress;

    @Shadow(remap = false)
    protected int duration;

    @Shadow(remap = false)
    private boolean isActive;

    public RecipeLogicMixin(MetaMachine machine) {
        super(machine);
    }

    /**
     * @author YiranMushroom
     * @reason Try to make this faster
     */
    @Overwrite(remap = false)
    @SuppressWarnings("all")
    public void onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            lastRecipe.postWorking(this.machine);
            handleRecipeIO(lastRecipe, IO.OUT);
            if (gtceuao$shouldTryModifyRecipe()) {
                if (lastOriginRecipe != null) {
                    var modified = machine.fullModifyRecipe(lastOriginRecipe);
                    if (modified == null) {
                        markLastRecipeDirty();
                    } else {
                        lastRecipe = modified;
                    }
                } else {
                    markLastRecipeDirty();
                }
            }
            // try it again
            if (!recipeDirty &&
                lastRecipe.matchRecipe(this.machine).isSuccess() &&
                lastRecipe.matchTickRecipe(this.machine).isSuccess() &&
                lastRecipe.checkConditions((RecipeLogic) (Object)this).isSuccess()) {
                setupRecipe(lastRecipe);
            } else {
                setStatus(RecipeLogic.Status.IDLE);
                progress = 0;
                duration = 0;
                isActive = false;
            }
        }
    }

    @Unique
    private boolean gtceuao$shouldTryModifyRecipe() {
        if (new Random().nextInt(AOConfigHolder.INSTANCE.machines.randomFactor) >= lastRecipe.duration)
            return false;
        return machine.alwaysTryModifyRecipe();
    }
}
