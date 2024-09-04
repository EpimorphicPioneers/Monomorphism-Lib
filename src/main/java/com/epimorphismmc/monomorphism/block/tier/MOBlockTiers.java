package com.epimorphismmc.monomorphism.block.tier;

import com.epimorphismmc.monomorphism.MonoLib;

import java.util.List;

public class MOBlockTiers {

    public static final BlockTierRegistry<VoltageTier> MACHINES = BlockTierManager.INSTANCE.createRegistry(MonoLib.id("machines"));

    public static final BlockTierRegistry<ICoilTier> COILS = BlockTierManager.INSTANCE.createRegistry(MonoLib.id("coils"));

    public static void init() {
        MACHINES.registerAll(List.of(VoltageTier.values()));
        COILS.registerAll(CoilTier.tiers());
    }
}
