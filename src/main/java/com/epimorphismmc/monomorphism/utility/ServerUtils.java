package com.epimorphismmc.monomorphism.utility;

import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;

import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ServerUtils {

    /**
     * Since in a Minecraft client, multiple servers can be launched and stopped during a single session, the result of
     * this method should not be stored globally.
     *
     * @return The currently running Minecraft server instance, if there is one.
     */
    public static @Nullable MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    /**
     * @return A list of all players in the game. On the client it'll be empty if no level is loaded.
     */
    public static List<ServerPlayer> getPlayers() {
        var server = getServer();
        if (server != null) {
            return server.getPlayerList().getPlayers();
        }
        return Collections.emptyList();
    }

    public static @Nullable ServerPlayer getPlayer(UUID uuid) {
        var server = getServer();
        if (server != null && uuid != Util.NIL_UUID) {
            return server.getPlayerList().getPlayer(uuid);
        }
        return null;
    }

    public static @Nullable GameProfile getGameProfile(UUID uuid) {
        var player = getPlayer(uuid);
        if (player != null) {
            return player.getGameProfile();
        }
        return Optional.ofNullable(getServer())
                .map(MinecraftServer::getProfileCache)
                .flatMap(cache -> cache.get(uuid))
                .orElse(null);
    }

    public static @Nullable String getPlayerName(UUID uuid) {
        var profile = getGameProfile(uuid);
        if (profile != null) {
            return profile.getName();
        }
        return null;
    }

    public static boolean isPlayerOP(UUID uuid) {
        var server = getServer();
        var profile = getGameProfile(uuid);
        if (server != null && profile != null) {
            return server.getPlayerList().isOp(profile);
        }
        return false;
    }

    public static boolean isPlayerOP(Player player) {
        var server = getServer();
        var profile = player.getGameProfile();
        if (server != null) {
            return server.getPlayerList().isOp(profile);
        }
        return false;
    }
}
