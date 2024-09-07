package com.epimorphismmc.monomorphism.integration.gtm.pattern;

import com.epimorphismmc.monomorphism.integration.gtm.pattern.predicates.PredicateDirections;
import com.epimorphismmc.monomorphism.integration.gtm.pattern.predicates.TierPredicateFactory;
import com.epimorphismmc.monomorphism.integration.gtm.pattern.utils.containers.IValueContainer;
import com.epimorphismmc.monomorphism.integration.gtm.pattern.utils.containers.SimpleValueContainer;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.PredicateBlocks;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MOPredicates {

    public static TraceabilityPredicate countBlock(String name, Block... blocks) {
        return enhancePredicate(
                name,
                () -> new SimpleValueContainer<>(0, (integer, block, tierType) -> ++integer),
                Predicates.blocks(blocks),
                null);
    }

    public static TraceabilityPredicate coilBlock() {
        return TierPredicateFactory.create("Coil")
                //                .map(MOBlockMaps.ALL_COIL_BLOCKS)
                .errorKey(Component.translatable("gtceu.multiblock.pattern.error.coils"))
                .strict(true)
                .build();
    }

    public static TraceabilityPredicate machineCasingBlock() {
        return TierPredicateFactory.create("MachineCasing")
                //                .map(MOBlockMaps.ALL_MACHINE_CASINGS)
                .strict(true)
                .build();
    }

    public static TraceabilityPredicate optionalPredicate(String mark, TraceabilityPredicate inner) {
        Predicate<MultiblockState> predicate = state -> {
            var context = state.getMatchContext();
            if (inner.test(state)) {
                return (context.getOrPut(mark, true));
            }
            return context.get(mark) == null;
        };
        BlockInfo[] candidates = inner.common.stream()
                .map(p -> p.candidates)
                .filter(Objects::nonNull)
                .map(Supplier::get)
                .flatMap(Arrays::stream)
                .toArray(BlockInfo[]::new);
        return new TraceabilityPredicate(new SimplePredicate(predicate, () -> candidates));
    }

    //    public static TraceabilityPredicate tierOptionalPredicate(String name, int tier,
    // TraceabilityPredicate inner) {
    //        return enhancePredicate(name, TierOptionalContainer::new, optionalPredicate(name + tier,
    // inner), tier);
    //    }

    public static TraceabilityPredicate enhancePredicate(
            String name,
            Supplier<IValueContainer<?>> containerSupplier,
            TraceabilityPredicate inner,
            @Nullable Object data) {
        Predicate<MultiblockState> predicate = state -> {
            if (inner.test(state)) {
                IValueContainer<?> currentContainer =
                        state.getMatchContext().getOrPut(name + "Value", containerSupplier.get());
                currentContainer.operate(state.getBlockState().getBlock(), data);
                return true;
            }
            return false;
        };
        BlockInfo[] candidates = inner.common.stream()
                .map(p -> p.candidates)
                .filter(Objects::nonNull)
                .map(Supplier::get)
                .flatMap(Arrays::stream)
                .toArray(BlockInfo[]::new);
        return new TraceabilityPredicate(new SimplePredicate(predicate, () -> candidates));
    }

    public static TraceabilityPredicate tierAbilities(String name, PartAbility... abilities) {
        return new TraceabilityPredicate(
                new PredicateBlocks(Arrays.stream(abilities)
                        .map(PartAbility::getAllBlocks)
                        .flatMap(Collection::stream)
                        .toArray(Block[]::new)) {
                    @Override
                    public boolean test(MultiblockState blockWorldState) {
                        if (super.test(blockWorldState)) {
                            if (MetaMachine.getMachine(blockWorldState.getWorld(), blockWorldState.getPos())
                                    instanceof ITieredMachine tieredMachine) {
                                int tier =
                                        blockWorldState.getMatchContext().getOrPut(name, tieredMachine.getTier());
                                return tier == tieredMachine.getTier();
                            }
                        }
                        return false;
                    }
                });
    }

    public static TraceabilityPredicate direction(Block block, RelativeDirection... directions) {
        return new TraceabilityPredicate(new PredicateDirections(block, directions));
    }
}
