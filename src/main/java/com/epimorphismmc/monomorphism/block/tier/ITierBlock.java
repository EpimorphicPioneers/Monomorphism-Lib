package com.epimorphismmc.monomorphism.block.tier;

public interface ITierBlock {
    default ITierType getTierType() {
        return null;
    }

    default void setTierType(ITierType type) {
        /**/
    }
}
