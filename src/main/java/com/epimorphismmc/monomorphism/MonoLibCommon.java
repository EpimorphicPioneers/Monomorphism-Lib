package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.data.pack.resource.CacheReloadManager;
import com.epimorphismmc.monomorphism.datagen.Datagen;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.jetbrains.annotations.Nullable;

import static com.epimorphismmc.monomorphism.utility.ServerUtils.getServer;

/**
 * Mod functionality that is common to both dedicated server and client.
 * <p>
 * Note that a client will still have zero or more embedded servers (although only one at a time).
 *
 * @author GateGuardian
 * @date : 2024/8/3
 */
public abstract class MonoLibCommon implements MonoLib {

    static MonoLib instance;

    public MonoLibCommon() {
        if (instance != null) {
            throw new IllegalStateException();
        }
        instance = this;

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(Datagen::init);
        MinecraftForge.EVENT_BUS.addListener(this::registerReloadListeners);
    }

    protected void registerReloadListeners(AddReloadListenerEvent event) {
        CacheReloadManager.INSTANCE.registerListener(PackType.SERVER_DATA, event::addListener);
    }

    @Override
    public @Nullable ResourceManager getResourceManager(PackType type) {
        if (type == PackType.SERVER_DATA) {
            var server = getServer();
            if (server != null) {
                return server.getResourceManager();
            }
        }
        return null;
    }

    @Override
    public @Nullable RecipeManager getRecipeManager() {
        var server = getServer();
        if (server != null) {
            return server.getRecipeManager();
        }
        return null;
    }

    @Override
    public RegistryAccess getRegistryAccess() {
        var server = getServer();
        if (server != null) {
            return server.registryAccess();
        }
        return RegistryAccess.EMPTY;
    }
}
