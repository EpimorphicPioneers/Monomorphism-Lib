package com.epimorphismmc.monomorphism.pattern.predicates;

import com.epimorphismmc.monomorphism.block.tier.ITierType;
import com.epimorphismmc.monomorphism.pattern.utils.containers.IValueContainer;
import com.epimorphismmc.monomorphism.utility.MOUtils;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TierPredicateFactory {

    private final TraceabilityPredicateType type;
    private final String tName;
    @Setter @Accessors(fluent = true, chain = true)
    private Object2ObjectOpenHashMap<ITierType, Supplier<Block>> map;
    @Setter @Accessors(fluent = true, chain = true)
    private Object2ObjectOpenHashMap<ITierType, Supplier<Block>> candidatesMap;
    @Setter @Accessors(fluent = true, chain = true)
    private Component errorKey;
    @Setter @Accessors(fluent = true, chain = true)
    private Comparator<ITierType> comparator;
    @Setter @Accessors(fluent = true, chain = true)
    private Predicate<ITierType> predicate;
    @Setter @Accessors(fluent = true, chain = true)
    private Supplier<IValueContainer<?>> container;
    private static final Map<String, BlockInfo[]> CACHE = new HashMap<>();

    protected TierPredicateFactory(TraceabilityPredicateType type, String name) {
        this.type = type;
        this.tName = name;
    }

    public static TierPredicateFactory create(TraceabilityPredicateType type, String name) {
        return new TierPredicateFactory(type, name);
    }

    public TraceabilityPredicate build() {
        return switch (type) {
            case TIER -> new TraceabilityPredicate(new EnhancePredicate(
                    getTierPredicate(tName,
                            MOUtils.getOrDefault(map, Object2ObjectOpenHashMap<ITierType, Supplier<Block>>::new),
                            MOUtils.getOrDefault(container, () -> IValueContainer::noop),
                            MOUtils.getOrDefault(errorKey, () -> Component.translatable("structure.multiblock.pattern.error.casing"))
                    ),
                    getCandidates(tName,
                            MOUtils.getOrDefault(candidatesMap != null ? candidatesMap : map, Object2ObjectOpenHashMap<ITierType, Supplier<Block>>::new),
                            MOUtils.getOrDefault(comparator, () -> Comparator.comparingInt(ITierType::tier)),
                            MOUtils.getOrDefault(predicate, () -> BlockState -> true)
                    )).setBlockTier(true));
            case LOOSE -> new TraceabilityPredicate(new EnhancePredicate(
                    getLoosePredicate(tName,
                            MOUtils.getOrDefault(candidatesMap != null ? candidatesMap : map, Object2ObjectOpenHashMap<ITierType, Supplier<Block>>::new),
                            MOUtils.getOrDefault(container, () -> IValueContainer::noop)
                    ),
                    getCandidates(tName,
                            MOUtils.getOrDefault(map, Object2ObjectOpenHashMap<ITierType, Supplier<Block>>::new),
                            MOUtils.getOrDefault(comparator, () -> Comparator.comparingInt(ITierType::tier)),
                            MOUtils.getOrDefault(predicate, () -> BlockState -> true)
                    )).setBlockTier(true));
        };
    }

    private Predicate<MultiblockState> getLoosePredicate(String name, Object2ObjectOpenHashMap<ITierType, Supplier<Block>> map, Supplier<IValueContainer<?>> containerSupplier) {
        return  (blockWorldState) -> {
            var blockState = blockWorldState.getBlockState();
            var objectIterator = map.object2ObjectEntrySet().fastIterator();
            while (objectIterator.hasNext()) {
                Object2ObjectMap.Entry<ITierType, Supplier<Block>> entry = objectIterator.next();
                if (blockState.is(entry.getValue().get())) {
                    IValueContainer<?> currentContainer = blockWorldState.getMatchContext().getOrPut(name + "Value", containerSupplier.get());
                    currentContainer.operate(blockState.getBlock(), entry.getKey());
                    return true;
                }
            }
            return false;
        };
    }

    private Predicate<MultiblockState> getTierPredicate(String name, Object2ObjectOpenHashMap<ITierType, Supplier<Block>> map, Supplier<IValueContainer<?>> containerSupplier, Component errorKey) {
        return  (blockWorldState) -> {
            var blockState = blockWorldState.getBlockState();
            var objectIterator = map.object2ObjectEntrySet().fastIterator();
            while (objectIterator.hasNext()) {
                Object2ObjectMap.Entry<ITierType, Supplier<Block>> entry = objectIterator.next();
                if (blockState.is(entry.getValue().get())) {
                    var stats = entry.getKey();
                    Object currentStats = blockWorldState.getMatchContext().getOrPut(name, stats);
                    if (!currentStats.equals(stats)) {
                        blockWorldState.setError(new PatternStringError(errorKey.getString()));
                        return false;
                    }
                    IValueContainer<?> currentContainer = blockWorldState.getMatchContext().getOrPut(name + "Value", containerSupplier.get());
                    currentContainer.operate(blockState.getBlock(), stats);
                    return true;
                }
            }
            return false;
        };
    }

    private Supplier<BlockInfo[]> getCandidates(String name, Object2ObjectOpenHashMap<ITierType, Supplier<Block>> map, Comparator<ITierType> comparator, Predicate<ITierType> predicate) {
        return () -> CACHE.computeIfAbsent(name, key -> map.keySet().stream()
                        .filter(predicate)
                        .sorted(comparator)
                        .map(type -> BlockInfo.fromBlock(map.get(type).get()))
                        .toArray(BlockInfo[]::new));
    }

    public enum TraceabilityPredicateType {
        TIER,
        LOOSE
    }
}
