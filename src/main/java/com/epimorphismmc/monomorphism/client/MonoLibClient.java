package com.epimorphismmc.monomorphism.client;

import com.epimorphismmc.monomorphism.MonoLibCommon;
import com.epimorphismmc.monomorphism.client.model.loader.ItemCustomLayerModel;
import com.epimorphismmc.monomorphism.client.utils.ClientUtils;
import com.epimorphismmc.monomorphism.utility.Platform;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.jetbrains.annotations.Nullable;

/**
 * Client-specific functionality.
 *
 * @author GateGuardian
 * @date : 2024/8/3
 */
@OnlyIn(Dist.CLIENT)
public class MonoLibClient extends MonoLibCommon {

    public MonoLibClient() {
        super();
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerGeometryLoaders);
    }

    private void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("item_custom_layers", ItemCustomLayerModel.Loader.INSTANCE);
    }

    @Override
    public @Nullable Level getClientLevel() {
        return ClientUtils.mc().level;
    }

    @Override
    public RegistryAccess getRegistryAccess() {
        if (Platform.getEffectiveSide().isClient()) {
            Level client = getClientLevel();
            return client == null ? RegistryAccess.EMPTY : client.registryAccess();
        } else {
            return super.getRegistryAccess();
        }
    }

    @Override
    public void queueTask(Runnable task) {
        if (Platform.getEffectiveSide().isClient()) {
            ClientUtils.mc().submit(task);
        } else {
            super.queueTask(task);
        }
    }
}
