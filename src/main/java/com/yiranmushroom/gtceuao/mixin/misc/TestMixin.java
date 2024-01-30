package com.yiranmushroom.gtceuao.mixin.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.world.level.GameType;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.yiranmushroom.gtceuao.gtceuao.LOGGER;

@Mixin(ClientPacketListener.class)
public abstract class TestMixin {
/*    @Final
    @Shadow
    @Mutable
    private final Minecraft minecraft;

    protected TestMixin(Minecraft minecraft) {
        this.minecraft = minecraft;
    }


    @Inject(method = "handleGameEvent", at = @At("RETURN"), remap = true)
    public void handleGameEvent(ClientboundGameEventPacket pPacket, CallbackInfo ci) {

        LOGGER.info("Player mayFly: " + String.valueOf(this.minecraft.player.getAbilities().mayfly));
    }*/
}
