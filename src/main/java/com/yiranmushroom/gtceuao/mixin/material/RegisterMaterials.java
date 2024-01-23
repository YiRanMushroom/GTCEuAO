package com.yiranmushroom.gtceuao.mixin.material;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.yiranmushroom.gtceuao.materials.Register;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(GTMaterials.class)
public class RegisterMaterials {
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/common/data/materials/SecondDegreeMaterials;register()V",shift = At.Shift.AFTER), remap = false)
    private static void initSecondDegreeMaterials(CallbackInfo ci) {
        LOGGER.info("Registering or Replacing Materials...");
        Register.secondTierMaterialRegister();
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/common/data/materials/MaterialFlagAddition;register()V",shift = At.Shift.AFTER), remap = false)
    private static void initMaterialFlagAddition(CallbackInfo ci) {
        LOGGER.info("Registering or Replacing Material Flags...");
        Register.additionalMaterialFlagsRegister();
    }

}
