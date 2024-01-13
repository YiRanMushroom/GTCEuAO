package com.yiranmushroom.gtceuao.config;

import com.gregtechceu.gtceu.config.ConfigHolder;

//import net.minecraft.util.text.TextComponentString;

import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/*import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.fml.common.gameevent.PlayerEvent;*/


@Mod.EventBusSubscriber

public class ConfigWarning {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (ConfigHolder.INSTANCE.compat.energy.nativeEUToPlatformNative) {
            var player = event.getEntity();
//            if (player instanceof net.minecraft.client.player.LocalPlayer)
            player.sendSystemMessage(Component.literal("You Should disable gregtech nativeEUToFE to run gtcefe, or CRASHES are expected.\nAlso, to apply that needs game restart."));
        }
    }
}