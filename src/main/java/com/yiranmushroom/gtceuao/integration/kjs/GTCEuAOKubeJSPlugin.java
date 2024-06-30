package com.yiranmushroom.gtceuao.integration.kjs;

import com.google.common.eventbus.EventBus;
import com.yiranmushroom.gtceuao.recipes.AORecipeModifiers;
import com.yiranmushroom.gtceuao.recipes.AORecipeTypes;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class GTCEuAOKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(BindingsEvent event) {
        super.registerBindings(event);
        event.add("AORecipeModifiers", AORecipeModifiers.class);
        event.add("AORecipeTypes", AORecipeTypes.class);
    }
}
