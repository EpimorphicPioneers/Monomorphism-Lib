package com.epimorphismmc.monomorphism.block.tier;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

public class BlockTierManager {

    public static final BlockTierManager INSTANCE = new BlockTierManager();

    private final Object2ObjectMap<ResourceLocation, BlockTierRegistry<?>> registries = new Object2ObjectOpenHashMap<>();

    public void onItemTooltip(ItemTooltipEvent event) {

    }

    @NotNull
    public <T extends IBlockTier> BlockTierRegistry<T> createRegistry(@NotNull ResourceLocation location) {
        Preconditions.checkArgument(!registries.containsKey(location),
                "BlockTier registry already exists for location %s", location);
        BlockTierRegistry<T> registry = new BlockTierRegistry<>(location);
        registries.put(location, registry);
        return registry;
    }

    @NotNull
    public BlockTierRegistry<?> getRegistry(@NotNull ResourceLocation location) {
        return registries.get(location);
    }
}
