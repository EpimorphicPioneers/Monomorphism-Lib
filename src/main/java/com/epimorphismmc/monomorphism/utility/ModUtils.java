package com.epimorphismmc.monomorphism.utility;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Consumer;

public class ModUtils {

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

    public static void findAnnotatedClasses(
            Class<? extends Annotation> annotationClass,
            Consumer<Class<?>> consumer,
            Runnable onFinished) {
        org.objectweb.asm.Type annotationType = org.objectweb.asm.Type.getType(annotationClass);
        for (ModFileScanData data : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData annotation : data.getAnnotations()) {
                if (annotationType.equals(annotation.annotationType())) {
                    try {
                        consumer.accept(Class.forName(
                                annotation.memberName(), false, Thread.currentThread().getContextClassLoader()));
                    } catch (Throwable throwable) {
                        MonoLib.LOGGER.error(
                                "Failed to load class for notation: " + annotation.memberName(), throwable);
                    }
                }
            }
        }
        onFinished.run();
    }
}
