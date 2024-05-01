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

public class EnhancePredicate extends SimplePredicate {

    @Getter @Setter @Accessors(chain = true)
    private boolean isBlockTier;

    public EnhancePredicate() {
    }

    public EnhancePredicate(String type) {
        super(type);
    }

    public EnhancePredicate(Predicate<MultiblockState> predicate, @Nullable Supplier<BlockInfo[]> candidates) {
        super(predicate, candidates);
    }

    public EnhancePredicate(String type, Predicate<MultiblockState> predicate, @Nullable Supplier<BlockInfo[]> candidates) {
        super(type, predicate, candidates);
    }
}
