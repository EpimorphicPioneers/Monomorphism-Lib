package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.addon.events.KJSRecipeKeyEvent;
import com.gregtechceu.gtceu.api.addon.events.MaterialCasingCollectionEvent;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.minecraftforge.fml.ModList;

public abstract class MOGTAddon implements IGTAddon {

    private final MOMod<? extends ICommonProxyBase> instance;

    public MOGTAddon(String modid) {
        Object object = ModList.get().getModObjectById(modid).orElseThrow(() -> new RuntimeException("Unable to get mod instance."));
        if (object instanceof MOMod<? extends ICommonProxyBase>) {
            instance = (MOMod<? extends ICommonProxyBase>) object;
        } else {
            throw new IllegalStateException("Mod class should extend the MOMod class.");
        }
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
        instance.getProxy().registerTagPrefixes();
    }

    @Override
    public void collectMaterialCasings(MaterialCasingCollectionEvent event) {
        instance.getProxy().collectMaterialCasings(event);
    }

    @Override
    public void registerRecipeKeys(KJSRecipeKeyEvent event) {
        instance.getProxy().registerRecipeKeys(event);
    }

    @Override
    public void registerWorldgenLayers() {
        instance.getProxy().registerWorldgenLayers();
    }

    @Override
    public void registerVeinGenerators() {
        instance.getProxy().registerVeinGenerators();
    }

    @Override
    public void registerIndicatorGenerators() {
        instance.getProxy().registerIndicatorGenerators();
    }

}
