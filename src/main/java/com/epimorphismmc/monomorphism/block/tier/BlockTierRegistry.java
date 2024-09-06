package com.epimorphismmc.monomorphism.block.tier;

import com.epimorphismmc.monomorphism.utility.TagUtils;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMap;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTierRegistry<T extends IBlockTier> {

    @Getter
    private final ResourceLocation location;

    private final Object2ObjectSortedMap<T, TagKey<Block>> tierTagMap;

    BlockTierRegistry(ResourceLocation location) {
        this.location = location;
        this.tierTagMap = new Object2ObjectAVLTreeMap<>(IBlockTier.COMPARATOR);
    }

    public void register(T blockTier) {
        tierTagMap.put(
                blockTier,
                TagUtils.optionalTag(
                        ForgeRegistries.BLOCKS.getRegistryKey(),
                        location.withSuffix("/%s".formatted(blockTier.typeName()))));
    }

    public void registerAll(Collection<T> blockTiers) {
        for (T blockTier : blockTiers) {
            tierTagMap.put(
                    blockTier,
                    TagUtils.optionalTag(
                            ForgeRegistries.BLOCKS.getRegistryKey(),
                            location.withSuffix("/%s".formatted(blockTier.typeName()))));
        }
    }

    public @Nullable T getTier(BlockState state) {
        for (var entry : tierTagMap.entrySet()) {
            if (state.is(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public @Nullable T getTier(Block block) {
        for (var entry : tierTagMap.entrySet()) {
            if (isTag(block, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Collection<Block> getBlocks(T blockTier) {
        var tag = tierTagMap.get(blockTier);
        if (tag == null) {
            throw new IllegalArgumentException("Unregistered BlockTier!");
        }

        var manager = ForgeRegistries.BLOCKS.tags();
        if (manager != null) {
            return manager.getTag(tag).stream().collect(Collectors.toUnmodifiableSet());
        }
        return Collections.emptySet();
    }

    public TagKey<Block> getTag(T blockTier) {
        var tag = tierTagMap.get(blockTier);
        if (tag == null) {
            throw new IllegalArgumentException("Unregistered BlockTier!");
        }
        return tag;
    }

    public void handleTooltip(Block block, Player player, List<Component> list, TooltipFlag flags) {
        for (var entry : tierTagMap.entrySet()) {
            if (isTag(block, entry.getValue())) {
                list.addAll(entry.getKey().getTooltips(this));
            }
        }
    }

    private boolean isTag(Block block, TagKey<Block> tag) {
        return block.builtInRegistryHolder().is(tag);
    }
}
