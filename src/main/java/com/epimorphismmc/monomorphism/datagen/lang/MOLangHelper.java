package com.epimorphismmc.monomorphism.datagen.lang;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class MOLangHelper {

    public static List<MutableComponent> getTooltips(ItemLike item, int tooltips) {
        return getSubKeys(item.asItem().getDescriptionId() + ".desc", tooltips);
    }

    public static String getBlockKey(String modid, String key) {
        return "block.%s.%s".formatted(modid, key);
    }

    public static String getKey(String modid, String key, ResourceKey<Registry<?>> registry) {
        return "%s.%s.%s".formatted(registry.location().getPath(), modid, key);
    }

    public static List<MutableComponent> getSubKeys(String key, int tooltips) {
        List<MutableComponent> list = new ArrayList<>();
        if (tooltips <= 1) {
            list.add(Component.translatable(key));
            return list;
        } else {
            for (var i = 0; i < tooltips; i++) {
                list.add(Component.translatable(getSubKey(key, i)));
            }
        }
        return list;
    }

    public static String getSubKey(String key, int index) {
        return key + "." + index;
    }
}
