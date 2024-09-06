package com.epimorphismmc.monomorphism.block.tier;

import com.epimorphismmc.monomorphism.client.input.InputUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockTierManager {

    public static final BlockTierManager INSTANCE = new BlockTierManager();

    private final Object2ObjectMap<ResourceLocation, BlockTierRegistry<?>> registries =
            new Object2ObjectOpenHashMap<>();

    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {
        var item = event.getItemStack().getItem();
        var player = event.getEntity();
        if (player != null && item instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            var flags = event.getFlags();
            var tooltip = event.getToolTip();
            List<Component> components = new ArrayList<>();
            for (var registry : registries.values()) {
                registry.handleTooltip(block, player, components, flags);
                if (!InputUtils.isCtrlDown() && !components.isEmpty()) {
                    tooltip.add(Component.translatable("monomorphism.ctrl_info"));
                    return;
                }
            }
            if (!components.isEmpty()) {
                tooltip.addAll(components);
            }
        }
    }

    @NotNull public <T extends IBlockTier> BlockTierRegistry<T> createRegistry(
            @NotNull ResourceLocation location) {
        Preconditions.checkArgument(
                !registries.containsKey(location),
                "BlockTier registry already exists for location %s",
                location);
        BlockTierRegistry<T> registry = new BlockTierRegistry<>(location);
        registries.put(location, registry);
        return registry;
    }

    @NotNull public BlockTierRegistry<?> getRegistry(@NotNull ResourceLocation location) {
        return registries.get(location);
    }
}
