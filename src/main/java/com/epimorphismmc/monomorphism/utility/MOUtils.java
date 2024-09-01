package com.epimorphismmc.monomorphism.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class MOUtils {
    //  Utils
    public static int getOrDefault(CompoundTag tag, String key, int defaultValue) {
        if (tag.contains(key)) {
            return tag.getInt(key);
        }
        return defaultValue;
    }

    public static <T> T getOrDefault(BooleanSupplier canGet, Supplier<T> getter, T defaultValue) {
        return canGet.getAsBoolean() ? getter.get() : defaultValue;
    }

    public static <T> T getOrDefault(List<T> canGet, int index, T defaultValue) {
        return index < canGet.size() ? canGet.get(index) : defaultValue;
    }

    public static <T> T getOrDefault(T[] canGet, int index, T defaultValue) {
        return index < canGet.length ? canGet[index] : defaultValue;
    }

    public static <T> T getOrLast(List<T> canGet, int index) {
        return getOrDefault(canGet, index, canGet.get(canGet.size() - 1));
    }

    public static <T> T getOrLast(T[] canGet, int index) {
        return getOrDefault(canGet, index, canGet[canGet.length - 1]);
    }

    //  List Utils
    public static <T> int maxLength(List<List<T>> lists) {
        return lists.stream().mapToInt(List::size).max().orElse(0);
    }

    public static <T> List<T> consistentList(List<T> list, int length) {
        if (list.size() >= length) {
            return list;
        }
        List<T> finalList = new ArrayList<>(list);
        T last = list.get(list.size() - 1);
        for (int i = 0; i < length - list.size(); i++) {
            finalList.add(last);
        }
        return finalList;
    }

    public static int maxLength(List<?>[] lists) {
        return Arrays.stream(lists).mapToInt(List::size).max().orElse(0);
    }

    //  Array Utils
    public static <T> void add(T[] a, T val) {
        for (int i = 0; i < a.length; ++i) {
            if (a[i] == null) {
                a[i] = val;
                return;
            }
        }
    }

    //  BitOperation Utils
    /**
     * 判断第几位是否为1
     *
     * @param num   整数
     * @param index 低位起第几位下标
     * @return 是否为1
     */
    public static boolean getStatusType(int num, int index) {
        return (num >> (index - 1) & 1) == 1;
    }

    /**
     * @param num    整数
     * @param index  低位起第几位下标
     * @param status 要修改的状态
     * @return 修改后的整数
     */
    public static int updateStatusType(int num, int index, int status) {
        if (status != 0 && status != 1) {
            throw new IllegalArgumentException();
        }
        if (status == 1) {
            // 1向左移(index-1) 和10010 或
            return (1 << (index - 1)) | num;
        } else {
            // 先判断原来是不是0,原来是0则直接返回
            if (!getStatusType(num, index)) {
                return num;
            }
            // 10010 - 1向左移(index-1)
            return num - (1 << (index - 1));
        }
    }

    public static boolean addToInventoryOrDrop(
            ItemStack stack, Level level, BlockPos pos, @Nullable Player player) {
        if (!level.isClientSide && !stack.isEmpty()) {
            if (player != null) {
                if (player.addItem(stack)) {
                    return true;
                }
            }

            ItemEntity entity =
                    new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
            return level.addFreshEntity(entity);
        }
        return false;
    }
}
