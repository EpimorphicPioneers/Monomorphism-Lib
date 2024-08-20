package com.epimorphismmc.monomorphism.utility;

public class ReflectionUtils {

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
}
