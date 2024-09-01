package com.epimorphismmc.monomorphism.utility;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.forgespi.language.IModInfo;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
        var modList = ModList.get();
        if (modList == null) {
            return LoadingModList.get().getMods().stream().map(ModInfo::getModId).anyMatch(modId::equals);
        }
        return modList.isLoaded(modId);
    }

    public static Optional<IModInfo> getModInfo(String modId) {
        var modList = ModList.get();
        if (modList == null) {
            return LoadingModList.get().getMods().stream()
                    .filter(info -> info.getModId().equals(modId))
                    .map(IModInfo.class::cast)
                    .findAny();
        }
        return modList.getModContainerById(modId).map(ModContainer::getModInfo);
    }

    public static String getModName(String modId) {
        return getModInfo(modId).map(IModInfo::getDisplayName).orElse(modId);
    }

    public static @Nullable ArtifactVersion getModVersion(String modId) {
        return getModInfo(modId).map(IModInfo::getVersion).orElse(null);
    }

    public static Path getGamePath() {
        return FMLPaths.GAMEDIR.get();
    }

    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
