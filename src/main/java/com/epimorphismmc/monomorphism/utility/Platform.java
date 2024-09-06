package com.epimorphismmc.monomorphism.utility;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Platform {

    public static boolean isDevelopment() {
        return !FMLEnvironment.production;
    }

    public static boolean isProduction() {
        return FMLEnvironment.production;
    }

    public static boolean isDatagen() {
        return FMLLoader.getLaunchHandler().isData();
    }

    public static Path getGamePath() {
        return FMLPaths.GAMEDIR.get();
    }

    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
