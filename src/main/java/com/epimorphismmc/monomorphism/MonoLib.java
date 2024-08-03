package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.utility.DistLogger;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;

public interface MonoLib {

    String MODID = "monomorphism";
    String NAME = "Monomorphism";
    Logger LOGGER = new DistLogger(NAME);

    static MonoLib instance() {
        return MonoLibCommon.instance;
    }

    static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, FormattingUtil.toLowerCaseUnder(path));
    }

    /**
     * Can be used to get the current level the client is in.
     *
     * @return null if no client level is available (i.e. on a dedicated server)
     */
    @Nullable Level getClientLevel();

    /**
     * Since in a Minecraft client, multiple servers can be launched and stopped during a single session, the result of
     * this method should not be stored globally.
     *
     * @return The currently running Minecraft server instance, if there is one.
     */
    @Nullable MinecraftServer getCurrentServer();

    /**
     * @return A stream of all players in the game. On the client it'll be empty if no level is loaded.
     */
    Collection<ServerPlayer> getPlayers();

    /**
     * @return a registry access object based on the logical side
     */
    RegistryAccess getRegistryAccess();

    /** Queues a task to be executed on this side */
    void queueTask(Runnable task);
}
