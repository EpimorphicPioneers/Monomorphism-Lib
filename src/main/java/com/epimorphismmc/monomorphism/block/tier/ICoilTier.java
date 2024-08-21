package com.epimorphismmc.monomorphism.block.tier;

import com.gregtechceu.gtceu.api.block.ICoilType;

public interface ICoilTier extends ICoilType, IBlockTier {

    @Override
    default String typeName() {
        return getName();
    }
}
