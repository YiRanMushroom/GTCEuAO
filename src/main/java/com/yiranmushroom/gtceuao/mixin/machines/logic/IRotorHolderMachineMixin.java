//package com.yiranmushroom.gtceuao.mixin.machines.logic;
//
//import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
//import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
//import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
//import com.yiranmushroom.gtceuao.gtceuao;
//import net.minecraft.world.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import static com.yiranmushroom.gtceuao.config.AOConfigHolder.INSTANCE;
//
//@Mixin(IRotorHolderMachine.class)
//public abstract interface IRotorHolderMachineMixin {
//    @Shadow(remap = false)
//    public static int SPEED_INCREMENT = INSTANCE.machines.buffTurbines ? 10 : 1;
//
//    @Shadow(remap = false)
//    public static int SPEED_DECREMENT = 3;
//
//    @Shadow(remap = false)
//    public abstract int getTierDifference();
//
//    @Shadow(remap = false)
//    public abstract int getRotorEfficiency();
//
//    @Shadow(remap = false)
//    public abstract int getHolderEfficiency();
//
//    @Shadow(remap = false)
//    public static int getBaseEfficiency() {
//        return 100;
//    }
//
//    @Shadow(remap = false)
//    public abstract ItemStack getRotorStack();
//
//    @Shadow(remap = false)
//    public abstract void setRotorStack(ItemStack stack);
//
//    /**
//     * @author YiranMushroom
//     * @reason Mixin not supporting inject in interface
//     */
//    @Overwrite(remap = false)
//    public default void damageRotor(int damageAmount) {
//        throw new RuntimeException("Mixin did apply");
////        if (INSTANCE.machines.disableRotorDamage) {
////            gtceuao.LOGGER.info("Rotor damage disabled");
////            return;
////        }
////        ItemStack stack = this.getRotorStack();
////        TurbineRotorBehaviour behavior = TurbineRotorBehaviour.getBehaviour(stack);
////        if (behavior != null) {
////            behavior.applyRotorDamage(stack, damageAmount);
////            this.setRotorStack(stack);
////        }
//    }
//
///*    @Inject(method = "damageRotor", at = @At("HEAD"), cancellable = true, remap = false)
//    public default void damageRotorInj(int damageAmount, CallbackInfo ci) {
//        if (INSTANCE.machines.disableRotorDamage) {
//            gtceuao.LOGGER.info("Rotor damage disabled");
//            ci.cancel();
//        }
//    }*/
//
//        /**
//     * @author YiranMushroom
//     * @reason Mixin not supporting inject in interface
//     */
//    @Overwrite(remap = false)
//    public default int getTotalEfficiency() {
//        throw new RuntimeException("Mixin did apply");
////        gtceuao.LOGGER.info("Successfully overwrote getTotalEfficiency");
////        int rotorEfficiency = this.getRotorEfficiency();
////        if (rotorEfficiency == -1) {
////            return -1;
////        } else {
////            int holderEfficiency = this.getHolderEfficiency();
////            return (holderEfficiency == -1 ? -1 : Math.max(getBaseEfficiency(), rotorEfficiency * holderEfficiency / 100)) * (INSTANCE.machines.buffTurbines ? (int) Math.pow(4.0, getTierDifference() + 1) : 1);
////        }
//    }
///*    @Inject(method = "getTotalEfficiency", at = @At("RETURN"), cancellable = true, remap = false)
//    public default void getTotalEfficiencyInj(CallbackInfoReturnable<Integer> cir) {
//        if (INSTANCE.machines.buffTurbines) {
//            gtceuao.LOGGER.info("Successfully injected getTotalEfficiency");
//            cir.setReturnValue(cir.getReturnValue() * (int) Math.pow(4.0, getTierDifference() + 1));
//        }
//    }*/
//}
