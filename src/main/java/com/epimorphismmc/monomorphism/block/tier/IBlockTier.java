package com.epimorphismmc.monomorphism.block.tier;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;

public interface IBlockTier {

    Comparator<IBlockTier> COMPARATOR = Comparator.comparingInt(IBlockTier::tier);

    String typeName();

    int tier();

    default List<Component> getTooltips(BlockTierRegistry<?> registry) {
        var location = registry.getLocation();
        var key = "block_tier.%s.%s".formatted(location.getNamespace(), location.getPath());
        return Lists.newArrayList(Component.translatable(key, Component.translatable(key + "." + typeName())));
    }
}
