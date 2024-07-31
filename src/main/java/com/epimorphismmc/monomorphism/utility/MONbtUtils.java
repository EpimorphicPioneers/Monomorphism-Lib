package com.epimorphismmc.monomorphism.utility;

import net.minecraft.nbt.CompoundTag;

public class MONbtUtils {

    public static void putBoolArray(CompoundTag tag, String key, boolean[] array) {
        byte[] bytes = new byte[array.length];
        for (byte i = 0; i < array.length; i++) {
            bytes[i] = (byte) (array[i] ? 1 : 0);
        }
        tag.putByteArray(key, bytes);
    }

    public static boolean[] getBoolArray(CompoundTag tag, String key) {
        byte[] bytes = tag.getByteArray(key);
        boolean[] array = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            array[i] = bytes[i] > 0;
        }
        return array;
    }
}
