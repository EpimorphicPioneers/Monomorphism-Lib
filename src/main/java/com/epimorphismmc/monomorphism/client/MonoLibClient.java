package com.epimorphismmc.monomorphism.client;

import com.epimorphismmc.monomorphism.MonoLibCommon;
import com.epimorphismmc.monomorphism.client.model.loader.ItemCustomLayerModel;
import com.epimorphismmc.monomorphism.client.utils.ClientUtils;
import com.epimorphismmc.monomorphism.data.pack.resource.CacheReloadManager;
import com.epimorphismmc.monomorphism.utility.SideUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
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
        bus.addListener(this::registerClientReloadListeners);
    }

    private void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("item_custom_layers", ItemCustomLayerModel.Loader.INSTANCE);
    }

    private void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        CacheReloadManager.INSTANCE.registerListener(
                PackType.CLIENT_RESOURCES, event::registerReloadListener);
    }

    @Override
    public @Nullable ResourceManager getResourceManager(PackType type) {
        if (type == PackType.SERVER_DATA) {
            super.getResourceManager(type);
        } else {
            return ClientUtils.mc().getResourceManager();
        }
        return null;
    }

    @Override
    public RecipeManager getRecipeManager() {
        var manager = super.getRecipeManager();
        var connection = ClientUtils.connection();
        if (manager == null && connection != null) {
            manager = connection.getRecipeManager();
        }
        return manager;
    }

    @Override
    public RegistryAccess getRegistryAccess() {
        if (SideUtils.getEffectiveSide().isClient()) {
            return SideUtils.getClientLevel().map(Level::registryAccess).orElse(RegistryAccess.EMPTY);
        } else {
            return super.getRegistryAccess();
        }
    }
}
