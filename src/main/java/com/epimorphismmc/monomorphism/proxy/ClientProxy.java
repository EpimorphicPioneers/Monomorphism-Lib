package com.epimorphismmc.monomorphism.proxy;

import com.epimorphismmc.monomorphism.client.renderer.item.MOItemRenderers;
import com.epimorphismmc.monomorphism.client.renderer.model.ItemCustomLayerModel;
import com.epimorphismmc.monomorphism.proxy.base.IClientProxyBase;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy implements IClientProxyBase {
    public ClientProxy() {
        super();
        MOItemRenderers.init();
    }

    @Override
    public void registerModBusEventHandlers(IEventBus bus) {
        super.registerModBusEventHandlers(bus);
        registerClientModBusEventHandlers(bus);
    }

    @Override
    public void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("item_custom_layers", ItemCustomLayerModel.Loader.INSTANCE);
    }
}
