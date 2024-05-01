package com.epimorphismmc.monomorphism.block;

import com.epimorphismmc.monomorphism.block.tier.ITierType;

public interface ITierBlock {
    default ITierType getTierType() {
        return null;
    }

    default void setTierType(ITierType type) {/**/}
}
