package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase;

import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.addon.events.KJSRecipeKeyEvent;
import com.gregtechceu.gtceu.api.addon.events.MaterialCasingCollectionEvent;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

public abstract class MOGTAddon implements IGTAddon {

    private final String modid;
    private MOMod<? extends ICommonProxyBase> instance;

    public MOGTAddon(String modid) {
        this.modid = modid;
        var modList = ModList.get();
        modList.getModObjectById(modid).ifPresentOrElse(this::setInstance, () -> modList
                .getModContainerById(modid)
                .map(FMLModContainer.class::cast)
                .map(FMLModContainer::getEventBus)
                .ifPresent(bus -> bus.addListener(this::onModConstructed)));
    }

    private void onModConstructed(FMLConstructModEvent event) {
        Object object = ModList.get()
                .getModObjectById(modid)
                .orElseThrow(() -> new RuntimeException("Unable to get mod instance."));
        setInstance(object);
    }

    private void setInstance(Object object) {
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
        return modid;
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
