package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.proxy.base.IProxyBase;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.addon.events.KJSRecipeKeyEvent;
import com.gregtechceu.gtceu.api.addon.events.MaterialCasingCollectionEvent;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

public abstract class MOGTAddon implements IGTAddon {

    private static MOMod<? extends IProxyBase> instance;

    public static void initialize(MOMod<? extends IProxyBase> mod) {
        instance = mod;
    }

    @Override
    public GTRegistrate getRegistrate() {
        return instance.getRegistrate();
    }

    @Override
    public String addonModId() {
        return instance.getModId();
    }

    @Override
    public boolean requiresHighTier() {
        return instance.requiresHighTier();
    }

    @Override
    public void registerTagPrefixes() {
        instance.proxy().registerTagPrefixes();
    }

    @Override
    public void collectMaterialCasings(MaterialCasingCollectionEvent event) {
        instance.proxy().collectMaterialCasings(event);
    }

    @Override
    public void registerRecipeKeys(KJSRecipeKeyEvent event) {
        instance.proxy().registerRecipeKeys(event);
    }

    @Override
    public void registerWorldgenLayers() {
        instance.proxy().registerWorldgenLayers();
    }

    @Override
    public void registerVeinGenerators() {
        instance.proxy().registerVeinGenerators();
    }

    @Override
    public void registerIndicatorGenerators() {
        instance.proxy().registerIndicatorGenerators();
    }

}
