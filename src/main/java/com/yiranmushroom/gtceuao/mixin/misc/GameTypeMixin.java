package com.yiranmushroom.gtceuao.mixin.misc;

import com.yiranmushroom.gtceuao.config.AOConfigHolder;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;
import static net.minecraft.world.level.GameType.*;

@Mixin(GameType.class)
public class GameTypeMixin {

    @Inject(method = "updatePlayerAbilities", at = @At("RETURN"), remap = true)
    public void updatePlayerAbilities(Abilities pAbilities, CallbackInfo ci) {

//        LOGGER.info("Injecting updatePlayerAbilities.");

        if (AOConfigHolder.INSTANCE.misc.flyAlwaysEnabled) {
            pAbilities.mayfly = true;
//            LOGGER.info("Fly should always be enabled.");
        }
    }
}
