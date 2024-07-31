package com.epimorphismmc.monomorphism.proxy;

import com.epimorphismmc.monomorphism.block.MOBlockMaps;
import com.epimorphismmc.monomorphism.capability.CapabilityHandler;
import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase;
import com.epimorphismmc.monomorphism.syncdata.MOSyncedFieldAccessors;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy implements ICommonProxyBase {
    public CommonProxy() {
//        MOSyncedFieldAccessors.init();
    }

    @Override
    public void registerModBusEventHandlers(IEventBus bus) {
        ICommonProxyBase.super.registerModBusEventHandlers(bus);
        bus.addListener(CapabilityHandler.getInstance()::registerCapabilities);
    }

    @Override
    public void onCommonSetupEvent(FMLCommonSetupEvent event) {
        MOBlockMaps.init();
    }

    @Override
    public void registerEventHandlers() {}

    @Override
    public void registerCapabilities() {}
}
