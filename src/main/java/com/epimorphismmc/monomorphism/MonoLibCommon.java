package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.block.MOBlockMaps;
import com.epimorphismmc.monomorphism.datagen.Datagen;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

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
        bus.register(this);
    }

    @SubscribeEvent
    private void onCommonSetupEvent(FMLCommonSetupEvent event) {
        MOBlockMaps.init();
    }

    @Override
    public @Nullable MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public RegistryAccess getRegistryAccess() {
        var server = getCurrentServer();
        if (server != null) {
            return server.registryAccess();
        }
        return RegistryAccess.EMPTY;
    }

    @Override
    public Collection<ServerPlayer> getPlayers() {
        var server = getCurrentServer();
        if (server != null) {
            return server.getPlayerList().getPlayers();
        }
        return Collections.emptyList();
    }

    @Override
    public void queueTask(Runnable task) {
        var server = getCurrentServer();
        if (server != null) {
            server.submit(new TickTask(server.getTickCount() + 1, task));
        }
    }
}
