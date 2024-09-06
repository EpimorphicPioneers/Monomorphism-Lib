package com.epimorphismmc.monomorphism.integration.gtm.pattern.predicates;

import com.epimorphismmc.monomorphism.block.tier.ITierType;
import com.epimorphismmc.monomorphism.integration.gtm.pattern.utils.containers.IValueContainer;
import com.epimorphismmc.monomorphism.block.tier.IBlockTier;
import com.epimorphismmc.monomorphism.pattern.utils.containers.IValueContainer;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNullElseGet;

@Setter
@Accessors(fluent = true, chain = true)
public class TierPredicateFactory {
    private final String name;
    private boolean strict;
    private Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>> map;
    private Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>> candidatesMap;
    private Component errorKey;
    private Comparator<IBlockTier> comparator;
    private Predicate<IBlockTier> predicate;
    private Supplier<IValueContainer<?>> container;

    private static final Map<String, BlockInfo[]> CACHE = new HashMap<>();

    protected TierPredicateFactory(String name) {
        this.name = name;
    }

    public static TierPredicateFactory create(String name) {
        return new TierPredicateFactory(name);
    }

    public TraceabilityPredicate build() {
        return strict
                ? new TraceabilityPredicate(new MOPredicate(
                                getStrictPredicate(
                                        name,
                                        requireNonNullElseGet(
                                                map, Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>>::new),
                                        requireNonNullElseGet(container, () -> IValueContainer::noop),
                                        requireNonNullElseGet(
                                                errorKey,
                                                () -> Component.translatable("structure.multiblock.pattern.error.casing"))),
                                getCandidates(
                                        name,
                                        requireNonNullElseGet(
                                                candidatesMap != null ? candidatesMap : map,
                                                Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>>::new),
                                        requireNonNullElseGet(
                                                comparator, () -> Comparator.comparingInt(IBlockTier::tier)),
                                        requireNonNullElseGet(predicate, () -> BlockState -> true)))
                        .previewCandidates(true))
                : new TraceabilityPredicate(new MOPredicate(
                                getPredicate(
                                        name,
                                        requireNonNullElseGet(
                                                candidatesMap != null ? candidatesMap : map,
                                                Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>>::new),
                                        requireNonNullElseGet(container, () -> IValueContainer::noop)),
                                getCandidates(
                                        name,
                                        requireNonNullElseGet(
                                                map, Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>>::new),
                                        requireNonNullElseGet(
                                                comparator, () -> Comparator.comparingInt(IBlockTier::tier)),
                                        requireNonNullElseGet(predicate, () -> BlockState -> true)))
                        .previewCandidates(true));
    }

    private Predicate<MultiblockState> getPredicate(
            String name,
            Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>> map,
            Supplier<IValueContainer<?>> containerSupplier) {
        return (blockWorldState) -> {
            var blockState = blockWorldState.getBlockState();
            var objectIterator = map.object2ObjectEntrySet().fastIterator();
            while (objectIterator.hasNext()) {
                Object2ObjectMap.Entry<IBlockTier, Supplier<Block>> entry = objectIterator.next();
                if (blockState.is(entry.getValue().get())) {
                    IValueContainer<?> currentContainer =
                            blockWorldState.getMatchContext().getOrPut(name + "Value", containerSupplier.get());
                    currentContainer.operate(blockState.getBlock(), entry.getKey());
                    return true;
                }
            }
            return false;
        };
    }

    private Predicate<MultiblockState> getStrictPredicate(
            String name,
            Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>> map,
            Supplier<IValueContainer<?>> containerSupplier,
            Component errorKey) {
        return (blockWorldState) -> {
            var blockState = blockWorldState.getBlockState();
            var objectIterator = map.object2ObjectEntrySet().fastIterator();
            while (objectIterator.hasNext()) {
                Object2ObjectMap.Entry<IBlockTier, Supplier<Block>> entry = objectIterator.next();
                if (blockState.is(entry.getValue().get())) {
                    var stats = entry.getKey();
                    Object currentStats = blockWorldState.getMatchContext().getOrPut(name, stats);
                    if (!currentStats.equals(stats)) {
                        blockWorldState.setError(new PatternStringError(errorKey.getString()));
                        return false;
                    }
                    IValueContainer<?> currentContainer =
                            blockWorldState.getMatchContext().getOrPut(name + "Value", containerSupplier.get());
                    currentContainer.operate(blockState.getBlock(), stats);
                    return true;
                }
            }
            return false;
        };
    }

    private Supplier<BlockInfo[]> getCandidates(
            String name,
            Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>> map,
            Comparator<IBlockTier> comparator,
            Predicate<IBlockTier> predicate) {
        return () -> CACHE.computeIfAbsent(name, key -> map.keySet().stream()
                .filter(predicate)
                .sorted(comparator)
                .map(type -> BlockInfo.fromBlock(map.get(type).get()))
                .toArray(BlockInfo[]::new));
    }
}
