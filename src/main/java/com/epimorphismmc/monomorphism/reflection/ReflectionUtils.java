package com.epimorphismmc.monomorphism.reflection;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {

    public static final StackWalker STACK_WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * Checks if the class file exists in the classpath without actually loading the class.
     *
     * @param className The fully qualified name of the class to check, including the package path, e.g., "com.example.MyClass".
     * @return true if the class file is found; false otherwise.
     */
    public static boolean isClassFound(String className) {
        var url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(className.replace('.', '/').concat(".class"));
        return url != null;
    }

    public static @Nullable Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public static Class<?> getRawType(Type type, Class<?> fallback) {
        var rawType = getRawType(type);
        return rawType != null ? rawType : fallback;
    }

    public static @Nullable Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof GenericArrayType) {
            return getRawType(((GenericArrayType) type).getGenericComponentType());
        } else if (type instanceof ParameterizedType) {
            return getRawType(((ParameterizedType) type).getRawType());
        } else {
            return null;
        }
    }
}
