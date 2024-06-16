package com.epimorphismmc.monomorphism.utility;

import com.epimorphismmc.monomorphism.Monomorphism;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MONBTUtils {
    public static CompoundTag writeBoolArray(String key, CompoundTag tag, boolean[] array) {
        byte[] bytes = new byte[array.length];
        for (byte i = 0; i < array.length; i++) {
            bytes[i] = (byte) (array[i] ? 1 : 0);
        }
        tag.putByteArray(key, bytes);
        return tag;
    }

    public static boolean[] readBoolArray(String key, CompoundTag tag) {
        byte[] bytes = tag.getByteArray(key);
        boolean[] array = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            array[i] = bytes[i] > 0;
        }
        return array;
    }

    public static CompoundTag writeItemStack(ItemStack itemStack, CompoundTag compoundTag) {
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        compoundTag.putString("id", resourceLocation.toString());
        compoundTag.putInt("Count", itemStack.getCount());
        if (itemStack.getTag() != null) {
            compoundTag.put("tag", itemStack.getTag().copy());
        }

        return compoundTag;
    }

    public static ItemStack readItemStack(CompoundTag compoundTag) {
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
            Monomorphism.instance.getLogger().debug("Tried to load invalid item: {}", compoundTag, ex);
            return ItemStack.EMPTY;
        }
    }
}
