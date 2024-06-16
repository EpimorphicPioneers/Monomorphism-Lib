package com.epimorphismmc.monomorphism.pattern.predicates;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class MOPredicate extends SimplePredicate {

    @Getter
    @Setter
    @Accessors(chain = true, fluent = true)
    private boolean previewCandidates;

    public MOPredicate() {}

    public MOPredicate(String type) {
        super(type);
    }

    public MOPredicate(
            Predicate<MultiblockState> predicate, @Nullable Supplier<BlockInfo[]> candidates) {
        super(predicate, candidates);
    }

    public MOPredicate(
            String type,
            Predicate<MultiblockState> predicate,
            @Nullable Supplier<BlockInfo[]> candidates) {
        super(type, predicate, candidates);
    }
}
