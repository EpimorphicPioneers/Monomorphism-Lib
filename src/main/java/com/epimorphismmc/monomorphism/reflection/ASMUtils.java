package com.epimorphismmc.monomorphism.reflection;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

public class ASMUtils {
    public static @Nullable Class<?> getClass(Type type) {
        return ReflectionUtils.getClass(
                isPrimitive(type) ? type.getClassName() : type.getInternalName().replace('/', '.'));
    }

    public static boolean isPrimitive(Type type) {
        return type.getSort() < Type.ARRAY;
    }
}
