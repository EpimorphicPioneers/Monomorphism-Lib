package com.epimorphismmc.monomorphism.utility;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.thread.EffectiveSide;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class Platform {

    /**
     * @return The physical side, is always Side.SERVER on the server and Side.CLIENT on the client
     */
    public static Dist getPhysicalSide() {
        return FMLEnvironment.dist;
    }

    /**
     * @return The effective side, on the server, this is always Side.SERVER, on the client it is dependent on the thread
     */
    public static LogicalSide getEffectiveSide() {
        return EffectiveSide.get();
    }

    public static boolean isDevelopment() {
        return !FMLEnvironment.production;
    }

    public static boolean isProduction() {
        return FMLEnvironment.production;
    }

    public static boolean isDatagen() {
        return FMLLoader.getLaunchHandler().isData();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Nullable public static String getModName(String modId) {
        return ModList.get()
                .getModContainerById(modId)
                .map(mc -> mc.getModInfo().getDisplayName())
                .orElse(modId);
    }

    public static Path getGamePath() {
        return FMLPaths.GAMEDIR.get();
    }

    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
