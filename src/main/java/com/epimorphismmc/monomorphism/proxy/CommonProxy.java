package com.epimorphismmc.monomorphism.proxy;

import com.epimorphismmc.monomorphism.capability.CapabilityHandler;
import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase;
import net.minecraftforge.eventbus.api.IEventBus;

public class CommonProxy implements ICommonProxyBase {
    public CommonProxy() {}

    @Override
    public void registerModBusEventHandlers(IEventBus bus) {
        ICommonProxyBase.super.registerModBusEventHandlers(bus);
        bus.addListener(CapabilityHandler.getInstance()::registerCapabilities);
    }

    @Override
    public void registerEventHandlers() {

    }

    @Override
    public void registerCapabilities() {

    }
}