package com.epimorphismmc.monomorphism.proxy;

import com.epimorphismmc.monomorphism.capability.CapabilityHandler;
import com.epimorphismmc.monomorphism.proxy.base.IProxyBase;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy extends IProxyBase {

    @Override
    default void registerModBusEventHandlers(IEventBus bus) {
        bus.addListener(CapabilityHandler.getInstance()::registerCapabilities);
    }

    @Override
    default void registerEventHandlers() {
    }

    @Override
    default void registerCapabilities() {

    }

    @Override
    default void onCommonSetupEvent(FMLCommonSetupEvent event) {
    }

    @Override
    default void onServerAboutToStartEvent(final ServerAboutToStartEvent event) {}

    default void forceClientRenderUpdate(BlockPos pos) {}

}
