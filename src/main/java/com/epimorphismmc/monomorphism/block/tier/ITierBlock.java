package com.epimorphismmc.monomorphism.block.tier;

public interface ITierBlock {
    default IBlockTier getTierType() {
        return null;
    }

    default void setTierType(IBlockTier type) {
        /**/
    }
}
