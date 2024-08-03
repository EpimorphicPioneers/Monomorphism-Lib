package com.epimorphismmc.monomorphism.utility;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    public static CompoundTag saveItemStack(CompoundTag compoundTag, ItemStack itemStack) {
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        compoundTag.putString("id", resourceLocation.toString());
        compoundTag.putInt("Count", itemStack.getCount());
        if (itemStack.getTag() != null) {
            compoundTag.put("tag", itemStack.getTag().copy());
        }

        return compoundTag;
    }

    public static ItemStack loadItemStack(CompoundTag compoundTag) {
        try {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(compoundTag.getString("id")));
            int count = compoundTag.getInt("Count");
            ItemStack stack = new ItemStack(item, count);
            if (compoundTag.contains("tag", Tag.TAG_COMPOUND)) {
                stack.setTag(compoundTag.getCompound("tag"));
                if (stack.getTag() != null) {
                    stack.getItem().verifyTagAfterLoad(stack.getTag());
                }
            }

            if (stack.getItem().canBeDepleted()) {
                stack.setDamageValue(stack.getDamageValue());
            }
            return stack;
        } catch (RuntimeException ex) {
            MonoLib.LOGGER.debug("Tried to load invalid item: {}", compoundTag, ex);
            return ItemStack.EMPTY;
        }
    }
}
