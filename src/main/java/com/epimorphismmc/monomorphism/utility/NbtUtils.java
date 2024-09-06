package com.epimorphismmc.monomorphism.utility;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NbtUtils {

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

    public static void putBlockPos(CompoundTag tag, String key, BlockPos pos) {
        tag.putIntArray(key, new int[] {pos.getX(), pos.getY(), pos.getZ()});
    }

    public static BlockPos getBlockPos(CompoundTag tag, String key) {
        int[] array = tag.getIntArray(key);
        return new BlockPos(array[0], array[1], array[2]);
    }

    /**
     * When the count of the ItemStack exceeds 127, please use this method instead of {@link ItemStack#save(CompoundTag)}.
     *
     * @param compoundTag The CompoundTag to which the ItemStack data will be saved.
     * @param itemStack The ItemStack to serialize.
     * @return The CompoundTag with the serialized ItemStack data.
     */
    public static CompoundTag saveItemStack(CompoundTag compoundTag, ItemStack itemStack) {
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        compoundTag.putString("id", resourceLocation.toString());
        compoundTag.putInt("Count", itemStack.getCount());
        if (itemStack.getTag() != null) {
            compoundTag.put("tag", itemStack.getTag().copy());
        }

        return compoundTag;
    }

    /**
     * When the count of the ItemStack exceeds 127, please use this method instead of {@link ItemStack#of(CompoundTag)}.
     *
     * @param compoundTag The CompoundTag from which to load the ItemStack data.
     * @return The deserialized ItemStack, or an empty ItemStack if the data is invalid.
     */
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
