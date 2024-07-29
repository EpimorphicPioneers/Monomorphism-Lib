package com.epimorphismmc.monomorphism.extensions.net.minecraft.nbt.CompoundTag;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.nbt.CompoundTag;

@Extension
public class CompoundTagExt {

    public static void putBoolArray(@This CompoundTag thiz, String key, boolean[] array) {
        byte[] bytes = new byte[array.length];
        for (byte i = 0; i < array.length; i++) {
            bytes[i] = (byte) (array[i] ? 1 : 0);
        }
        thiz.putByteArray(key, bytes);
    }

    public static boolean[] getBoolArray(@This CompoundTag thiz, String key) {
        byte[] bytes = thiz.getByteArray(key);
        boolean[] array = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            array[i] = bytes[i] > 0;
        }
        return array;
    }

}
