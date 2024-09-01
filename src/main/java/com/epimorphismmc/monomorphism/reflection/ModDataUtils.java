package com.epimorphismmc.monomorphism.reflection;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

public class ModDataUtils {

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
